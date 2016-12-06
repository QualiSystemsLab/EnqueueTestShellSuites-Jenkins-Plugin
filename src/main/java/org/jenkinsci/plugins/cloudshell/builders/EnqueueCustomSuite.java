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
package org.jenkinsci.plugins.cloudshell.builders;


import org.jenkinsci.plugins.cloudshell.CloudShellBuildStep;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import org.jenkinsci.plugins.cloudshell.Loggers.QsJenkinsTaskLogger;
import org.jenkinsci.plugins.cloudshell.SnQ_manager.SnQApiGateway;
import org.jenkinsci.plugins.cloudshell.TestShellBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;
import org.jenkinsci.plugins.cloudshell.SnQ_manager.TsServerDetails;

public class EnqueueCustomSuite extends CloudShellBuildStep
{

	private final String suiteName;
	private QsJenkinsTaskLogger logger;
	private SnQApiGateway snqManager;

	@DataBoundConstructor
	public EnqueueCustomSuite(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public boolean perform(final AbstractBuild<?, ?> build, final Launcher launcher, final BuildListener listener, TsServerDetails server) throws Exception {
		logger = new QsJenkinsTaskLogger(listener);
		SnQApiGateway gateway = new SnQApiGateway(logger,server);

		gateway.GetSuiteDetails(suiteName);

		return false;
	}


    @Extension
	public static final class enqueueCustomSuiteDescriptor extends CSBuildStepDescriptor{

		public enqueueCustomSuiteDescriptor() {
			load();
		}

		@Override
		public String getDisplayName() {
			return "Enqueue custom suite";
		}

	}	
}