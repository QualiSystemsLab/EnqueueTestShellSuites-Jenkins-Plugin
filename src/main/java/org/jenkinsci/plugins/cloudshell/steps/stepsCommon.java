package org.jenkinsci.plugins.cloudshell.steps;

import com.quali.cloudshell.QsExceptions.SandboxApiException;
import org.jenkinsci.plugins.cloudshell.SnQ_manager.TsServerDetails;
import com.quali.cloudshell.SandboxApiGateway;
import hudson.EnvVars;
import hudson.model.TaskListener;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.cloudshell.CloudShellConfig;
import org.jenkinsci.plugins.cloudshell.Loggers.QsJenkinsTaskLogger;
import org.jenkinsci.plugins.workflow.steps.StepContext;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class StepsCommon {
    public String StartSandbox(TaskListener listener, String name, int duration, StepContext context) throws SandboxApiException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, InterruptedException {
        listener.getLogger().println("CloudShell Starting!");
        SandboxApiGateway gateway = getSandboxApiGateway(listener);

        String sandboxName = null;
        EnvVars envVars = context.get(EnvVars.class);
        String jobName = envVars.get("JOB_NAME");
        if (jobName != null && !jobName.isEmpty())
        {
            sandboxName = jobName + "_" + java.util.UUID.randomUUID().toString().substring(0, 5);;
        }
        String sandboxId = gateway.startBlueprint(name, duration, true, sandboxName);
        return sandboxId;
    }

    public void StopSandbox(TaskListener listener, String sandboxId) throws SandboxApiException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        listener.getLogger().println("CloudShell Stop Starting!");
        SandboxApiGateway gateway = getSandboxApiGateway(listener);
        gateway.StopSandbox(sandboxId, true);
    }

    private SandboxApiGateway getSandboxApiGateway(TaskListener listener) {
        CloudShellConfig.DescriptorImpl descriptorImpl =
                (CloudShellConfig.DescriptorImpl) Jenkins.getInstance().getDescriptor(CloudShellConfig.class);
        TsServerDetails server = descriptorImpl.getServer();
        QsJenkinsTaskLogger logger = new QsJenkinsTaskLogger(listener);
       // return new SandboxApiGateway(logger, server);
        return null;
    }
}
