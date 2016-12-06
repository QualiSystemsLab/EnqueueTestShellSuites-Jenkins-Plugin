package org.jenkinsci.plugins.cloudshell.SnQ_manager;

import com.quali.cloudshell.HTTPWrapper;
import com.quali.cloudshell.QsServerDetails;

/**
 * Created by aharon.s on 12/4/2016.
 */
public class SnQManager
{
    private QsServerDetails serverDetails;

    public SnQManager(QsServerDetails serverDetails)
    {
        this.serverDetails = serverDetails;
        HTTPWrapper wrapper = new HTTPWrapper();
    }

    public QsServerDetails getServerDetails() {
        return serverDetails;
    }

    public boolean GetSuiteDetails (String suitenmae)
    {

        return true;

    }


}
