package org.jenkinsci.plugins.cloudshell;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.Launcher;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Items;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.cloudshell.SnQ_manager.TsServerDetails;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Created by aharon.s on 12/5/2016.
 */
public class TestShellConfig extends Builder {

    private final TestShellBuildStep buildStep;

    @DataBoundConstructor
    public TestShellConfig(final TestShellBuildStep buildStep)
    {
        this.buildStep = buildStep;
    }

    public TestShellBuildStep getBuildStep() {
        return buildStep;
    }

    @Override
    public boolean perform(final AbstractBuild<?, ?> build, final Launcher launcher, final BuildListener listener)  {
        TsServerDetails serverDetails = getDescriptor().getServer();
        try
        {
            return buildStep.perform(build, launcher, listener, serverDetails);

        } catch (Exception e) {
            listener.getLogger().println(e);
        }
        return false;
    }

    @Override
    public TestShellConfig.DescriptorImpl getDescriptor() {
        return (TestShellConfig.DescriptorImpl)super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private TsServerDetails server;

        public DescriptorImpl() {
            load();
        }

        @Initializer(before= InitMilestone.PLUGINS_STARTED)
        public static void addAliases() {
            Items.XSTREAM2.addCompatibilityAlias(
                    "org.jenkinsci.plugins.cloudshell.TestShellConfig",
                    TestShellConfig.class
            );
        }

        @Override
        public String getDisplayName() {
            return "SnQ Step";
        }

        public DescriptorExtensionList<TestShellBuildStep, TestShellBuildStep.TSBuildStepDescriptor> getBuildSteps() {
            return TestShellBuildStep.all();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            server = new TsServerDetails(
                    formData.getString("serverAddress"),
                    formData.getString("port"),
                    formData.getString("user"),
                    formData.getString("pw"),
                    formData.getString("domain"),
                    Boolean.parseBoolean(formData.getString("ignoreSSL"))
            );
            save();
            return super.configure(req,formData);
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        public String getServerAddress() {
            return server.serverAddress;
        }
        public String getUser() {
            return server.user;
        }
        public String getPw() {
            return server.pw;
        }
        public String getDomain() {
            return server.domain;
        }
        public boolean getIgnoreSSL() {
            return server.ignoreSSL;
        }
        public TsServerDetails getServer() {return server;}

    }
}

