/*   Copyright 2013, MANDIANT, Eric Lordahl
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.jenkinsci.plugins.cloudshell;

import org.jenkinsci.plugins.cloudshell.TSBuildStep.CSBuildStepDescriptor;

import org.jenkinsci.plugins.cloudshell.SnQ_manager.SnQApiGateway;
import org.jenkinsci.plugins.cloudshell.SnQ_manager.TsServerDetails;
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
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class TSConfig extends Builder {

	private final TSBuildStep buildStep;


	@DataBoundConstructor
	public TSConfig(final TSBuildStep buildStep)
	{
		this.buildStep = buildStep;

		TsServerDetails serverDetails = getDescriptor().getServer();
		this.buildStep.CsServer = new SnQApiGateway(null,serverDetails);
    }

	public TSBuildStep getBuildStep()
	{
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
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl)super.getDescriptor();
	}

	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private TsServerDetails server;

        public DescriptorImpl() {
            load();
        }

		@Initializer(before=InitMilestone.PLUGINS_STARTED)
        public static void addAliases() {
			Items.XSTREAM2.addCompatibilityAlias(
					"org.jenkinsci.plugins.cloudshell.TSConfig",
					TSConfig.class
			);
		}

		@Override
		public String getDisplayName() {
			return "TestShell Suite to enqueue";
		}

		public DescriptorExtensionList<TSBuildStep, CSBuildStepDescriptor> getBuildSteps() {
			return TSBuildStep.all();
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

		public String getServerAddress() {return server.serverAddress;}
		public String getPort() {return server.port;}
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
