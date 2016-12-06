
package org.jenkinsci.plugins.cloudshell.SnQ_manager;

import com.quali.cloudshell.*;
import com.quali.cloudshell.QsExceptions.ReserveBluePrintConflictException;
import com.quali.cloudshell.QsExceptions.SandboxApiException;
import net.sf.json.JSONObject;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * SnQ
 * Created by aharon.s on 12/5/2016.
 */
public class SnQAPIProxy
{
    private final TsServerDetails server;
    private final QsLogger logger;

    public SnQAPIProxy(TsServerDetails server, QsLogger logger) {
        this.server = server;
        this.logger = logger;
    }

    public String GetSuiteDetails(String suitename) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException
    {
        RestResponse response = this.Login();

        String result = response.getContent();
        return result;

    }

    public String StartBluePrint(String blueprintName, String sandboxName, int duration, boolean isSync) throws SandboxApiException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnsupportedEncodingException {
        RestResponse response = this.Login();
        String url = this.GetBaseUrl(true) + Constants.BLUEPRINTS_URI + URLEncoder.encode(blueprintName, "UTF-8") + "/start";
        StringEntity params = null;
        String sandboxDuration = "PT" + String.valueOf(duration) + "M";
        String string = "{\"name\":\"" + sandboxName + "\",\"duration\":\"" + sandboxDuration + "\"}";
        params = new StringEntity(string);
        JSONObject result = HTTPWrapper.ExecutePost(url, response.getContent(), params, this.server.ignoreSSL);
        String newSb;
        if(result.containsKey(Constants.ERROR_CATEGORY)) {
            newSb = result.get(Constants.MESSAGE).toString();
            if(newSb.equals(Constants.BLUEPRINT_CONFLICT_ERROR)) {
                throw new ReserveBluePrintConflictException(blueprintName, newSb);
            } else {
                this.logger.Info("ERROR: " + result);
                throw new SandboxApiException(blueprintName);
            }
        } else {
            newSb = result.getString("id");
            if(isSync) {
                this.WaitForSandBox(newSb, "Ready", 300, this.server.ignoreSSL);
            }

            return newSb;
        }
    }

    public void StopSandbox(String sandboxId, boolean isSync) throws SandboxApiException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RestResponse response = this.Login();
        String url = this.GetBaseUrl(true) + Constants.SANDBOXES_URI + sandboxId + "/stop";
        JSONObject result = HTTPWrapper.ExecutePost(url, response.getContent(), (StringEntity)null, this.server.ignoreSSL);
        if(result.containsKey(Constants.ERROR_CATEGORY)) {
            throw new SandboxApiException("Failed to stop blueprint: " + result);
        } else {
            try {
                if(isSync) {
                    this.WaitForSandBox(sandboxId, "Ended", 300, this.server.ignoreSSL);
                }
            } catch (Exception var7) {
                ;
            }

        }
    }

    private RestResponse Login() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return SnQHTTPWrapper.InvokeLogin(this.GetBaseUrl(false),Constants.SnQDefaultport ,this.server.user, this.server.pw, this.server.domain, this.server.ignoreSSL);
    }

    private void WaitForSandBox(String sandboxId, String status, int timeoutSec, boolean ignoreSSL) throws SandboxApiException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        long startTime = System.currentTimeMillis();

        for(String sandboxStatus = this.GetSandBoxStatus(sandboxId); !sandboxStatus.equals(status) && System.currentTimeMillis() - startTime < (long)(timeoutSec * 1000); sandboxStatus = this.GetSandBoxStatus(sandboxId)) {
            if(sandboxStatus.equals("Error")) {
                throw new SandboxApiException("Sandbox status is: Error");
            }

            try {
                Thread.sleep(1500L);
            } catch (InterruptedException var9) {
                var9.printStackTrace();
            }
        }

    }

    private String GetSandBoxStatus(String sb) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return this.SandboxDetails(sb).getString("state");
    }

    public JSONObject SandboxDetails(String sb) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RestResponse response = this.Login();
        String url = this.GetBaseUrl(true) + Constants.SANDBOXES_URI + sb;
        RestResponse result = HTTPWrapper.ExecuteGet(url, response.getContent(), this.server.ignoreSSL);
        JSONObject j = JSONObject.fromObject(result.getContent());
        if(j.toString().contains(Constants.ERROR_CATEGORY)) {
            throw new RuntimeException("Failed to get sandbox details: " + j);
        } else {
            return j;
        }
    }

    private String GetBaseUrl(boolean versioned) {
        return versioned?this.server.serverAddress + "/Api" + Constants.API_VERSION:this.server.serverAddress + "/Api";
    }
}
