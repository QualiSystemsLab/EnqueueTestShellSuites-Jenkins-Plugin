package org.jenkinsci.plugins.cloudshell.SnQ_manager;


import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * SnQ
 * Created by aharon.s on 12/4/2016.
 */
public class SnQApiGateway
{
    private final SnQAPIProxy proxy;
    private final QsLogger logger;

    public SnQApiGateway(String serverAddress, String user, String pw, String domain, boolean ignoreSSL, QsLogger qsLogger)
    {
        this.logger = qsLogger;
        this.proxy = new SnQAPIProxy(new TsServerDetails(serverAddress, user, pw, domain, ignoreSSL), qsLogger);
    }

    public SnQApiGateway(org.jenkinsci.plugins.cloudshell.SnQ_manager.QsLogger qsLogger, TsServerDetails qsServerDetails)
    {
        this.logger = qsLogger;
        this.proxy = new SnQAPIProxy(qsServerDetails, qsLogger);
    }

    public String GetSuiteDetails (String suitenmae) throws Exception
    {
        String result = getSuiteDetails(suitenmae);

        return result;

    }
    private String getSuiteDetails(String suitename) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException
    {
        return proxy.GetSuiteDetails(suitename);
    }


}
