package org.jenkinsci.plugins.cloudshell.action;

import hudson.model.Action;
import org.jenkinsci.plugins.cloudshell.SnQ_manager.TsServerDetails;

import java.io.Serializable;
import java.util.ArrayList;

public class SandboxLaunchAction implements Action, Serializable, Cloneable{

    private TsServerDetails serverDetails;
    private ArrayList<String> runningSandboxes = new ArrayList<String>();

    public SandboxLaunchAction(TsServerDetails serverDetails){

        this.serverDetails = serverDetails;
    }

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return null;
    }

    public TsServerDetails getServerDetails(){
        return this.serverDetails;
    }

    public void started(String sandboxId) {
        runningSandboxes.add( sandboxId );
    }

    public void stopped(String sandboxId) {
        runningSandboxes.remove( sandboxId );
    }

    public Iterable<String> getRunning() {
        return runningSandboxes;
    }
}
