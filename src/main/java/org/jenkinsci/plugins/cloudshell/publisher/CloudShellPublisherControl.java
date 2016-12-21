package org.jenkinsci.plugins.cloudshell.publisher;


import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import org.jenkinsci.plugins.cloudshell.Loggers.QsJenkinsTaskLogger;

import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.Serializable;
import org.jenkinsci.plugins.cloudshell.action.SandboxLaunchAction;
import java.util.List;

/**
 * Post-build step that allow stop all matched container
 *
 * @author magnayn
 */
public class CloudShellPublisherControl extends Recorder implements Serializable {

    @DataBoundConstructor
    public CloudShellPublisherControl() {
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Override
    public BuildStepDescriptor getDescriptor() {
        return super.getDescriptor();
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        List<SandboxLaunchAction> sandboxLaunchActions = build.getActions(SandboxLaunchAction.class);
         //QsJenkinsTaskLogger logger = new QsJenkinsTaskLogger(listener);
        QsJenkinsTaskLogger logger = new QsJenkinsTaskLogger(listener) {
        };


        return true;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Stop CloudShell sandboxes";
        }
    }
}


