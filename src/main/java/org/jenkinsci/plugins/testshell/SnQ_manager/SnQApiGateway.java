package org.jenkinsci.plugins.testshell.SnQ_manager;

import org.jenkinsci.plugins.testshell.Loggers.QsLogger;
import org.jenkinsci.plugins.testshell.Strcture.SuiteDetails;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * SnQ
 * Created by aharon.s on 12/4/2016.
 */
public class SnQApiGateway
{
    private final SnQAPIProxy proxy;
    private final QsLogger logger;

    public SnQApiGateway(String serverAddress,String port, String user, String pw, String domain, boolean ignoreSSL, QsLogger qsLogger)
    {
        this.logger = qsLogger;
        this.proxy = new SnQAPIProxy(new TsServerDetails(serverAddress,port, user, pw, domain, ignoreSSL), qsLogger);
    }

    public SnQApiGateway(QsLogger qsLogger, TsServerDetails qsServerDetails)
    {
        this.logger = qsLogger;
        this.proxy = new SnQAPIProxy(qsServerDetails, qsLogger);
    }

    public String GetSuiteDetails (String suiteName) throws Exception
    {
        return proxy.getSuiteJSON(suiteName);
    }

    public SuiteDetails EnqueuSuite(String suitename, String JSON)throws InterruptedException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException
    {
        return proxy.EnqueueSuite(suitename,JSON);
    }

    public List<String> getSuitesDetails() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException
    {
        return proxy.GetSuitesDetails();
    }


}
