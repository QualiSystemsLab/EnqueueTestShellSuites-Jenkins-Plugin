
package org.jenkinsci.plugins.cloudshell.SnQ_manager;

import com.quali.cloudshell.*;
import com.quali.cloudshell.QsExceptions.ReserveBluePrintConflictException;
import com.quali.cloudshell.QsExceptions.SandboxApiException;

import net.sf.json.JSONObject;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jenkinsci.plugins.cloudshell.Strcture.Suite;
import org.jenkinsci.plugins.cloudshell.Strcture.SuiteDetails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * SnQ
 * Created by aharon.s on 12/5/2016.
 */
public class SnQAPIProxy
{
    private final TsServerDetails server;
    private final org.jenkinsci.plugins.cloudshell.Loggers.QsLogger logger;
    public SnQHTTPWrapper wrapper;


    public SnQAPIProxy(TsServerDetails server, org.jenkinsci.plugins.cloudshell.Loggers.QsLogger logger) {
        this.server = server;
        this.logger = logger;
        wrapper = new SnQHTTPWrapper();
    }

    public boolean GetSuitesDetails(String suitename) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException
    {
        RestResponse response = this.Login();
        boolean suiteExists = false;

        if(! (response.getHttpCode()==200))
        {
            throw new IOException("Fail to login, please check your server parameters");
        }
        else
        {
            String token = response.getContent();
            String url = this.GetBaseUrl(false) + Constants.SUITES_URI;

            RestResponse suitesResponse = wrapper.ExecuteGet(url, token, true);

            if (suitesResponse.getHttpCode() == 200)
            {
                String suiteContent = suitesResponse.getContent();
                ObjectMapper mapper = new ObjectMapper();

                List<Suite> suites = mapper.readValue(suiteContent,mapper.getTypeFactory().constructCollectionType( List.class, Suite.class));

                if (suites.isEmpty())
                {
                    throw new IOException("No Suites found on CloudShell server, please check");
                }
                for(Suite s:suites)
                {
                    if(s.Name.equals(suitename))
                    {
                        suiteExists = true;
                    }
                }


            }

        }

        return suiteExists;

    }

    public String getSuiteJSON(String suiteName)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException
    {
        RestResponse response = this.Login();
        if(! (response.getHttpCode()==200))
        {
            throw new IOException("Fail to login, please check your server parameters");
        }
        else
        {
            String token = response.getContent();
            String url = this.GetBaseUrl(false) + Constants.SELECTED_SUITE_URI + suiteName ;
            String result = "";

            RestResponse suitesResponse = wrapper.ExecuteGet(url, token, true);

            if (suitesResponse.getHttpCode()!= 200)
            {
                throw new IOException("Fail to get suite details, suite name: " + suiteName);
            }
            else
            {
                result = suitesResponse.getContent();
            }
            return result;
        }
    }

    public SuiteDetails EnqueueSuite(String suitename,String JSON)throws KeyStoreException,InterruptedException, NoSuchAlgorithmException, KeyManagementException, IOException
    {
        SuiteDetails suiteDetails = null;

        RestResponse response = this.Login();
        if(! (response.getHttpCode()==200))
        {
            throw new IOException("Fail to login, please check your server parameters");
        }
        else
        {
            String token = response.getContent();
            String url = this.GetBaseUrl(false) + Constants.ENQUEUE_SUITE;
            String result = "";

            String modifiedJson= this.SetAPIJSON(JSON, suitename);
            StringEntity se = new StringEntity(modifiedJson, ContentType.APPLICATION_JSON);

            RestResponse suite_id  = wrapper.ExecutePost(url,token, se,true);

            if (suite_id.getHttpCode()!=200)
            {
                throw new IOException("Fail execute Suite: "+ suitename );
            }
            else
            {
                suiteDetails = waitForSuiteToFinishExecution(suite_id.getContent(),token);
            }

            return suiteDetails;
        }
    }

    private SuiteDetails waitForSuiteToFinishExecution(String jobExecutionId, String restToken)throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException, InterruptedException
    {
        SuiteDetails suiteDetails = null;
        boolean isSuiteRunning = true;
        ObjectMapper mapper = new ObjectMapper();
        RestResponse suiteDetailss;

        String url = this.GetBaseUrl(false) + Constants.GET_SUITE_DETAILS + jobExecutionId;
        url = url.replace("\"","");

        int poolingTime = Constants.POOLING_TIME_SECONDS_INTERVAL;

        while (isSuiteRunning)
        {
            suiteDetailss = wrapper.ExecuteGet(url, restToken, true);
            suiteDetails = mapper.readValue(suiteDetailss.getContent(), SuiteDetails.class);

            if (suiteDetails.SuiteStatus.equals("Ended"))
            {
                isSuiteRunning = false;
            }
            //Pause
            Thread.sleep(poolingTime* 1000);
        }

        return suiteDetails;
    }

    private String SetAPIJSON(String _JSON, String suiteTemplateNmae)
    {
        String JSON = _JSON;

        //clen end
        int domainIdlocation = JSON.indexOf(",\"DomainId");
        int endStr = JSON.length();
        String toRemove = JSON.substring(domainIdlocation,endStr);
        JSON = JSON.replace(toRemove,"}");

        //clean dates
        int createDate = JSON.indexOf("\"CreateDate");
        int emailNotification = JSON.indexOf("\"EmailNotifications");
        toRemove = JSON.substring(createDate,emailNotification);
        JSON = JSON.replace(toRemove,"");

        //replace decimal
        JSON= JSON.replace(".0","");

        //set suite name
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        String suiteName = dateFormat.format(date).toString();
        JSON = JSON.replace("\"SuiteName\":null","\"SuiteName\":\""+ suiteName + "\"");

        return JSON;
    }

    private RestResponse Login() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return wrapper.InvokeLogin(this.GetBaseUrl(false),this.server.user, this.server.pw, this.server.domain, this.server.ignoreSSL);
    }

    private String GetBaseUrl(boolean versioned)
    {
        return "http://" + this.server.serverAddress +":"+ Constants.SnQDefaultport +"/Api";
    }
}
