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

import org.jenkinsci.plugins.cloudshell.SnQ_manager.SnQApiGateway;
import org.jenkinsci.plugins.cloudshell.SnQ_manager.TsServerDetails;
import hudson.DescriptorExtensionList;
import hudson.ExtensionPoint;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Describable;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

public abstract class TSBuildStep implements Describable<TSBuildStep>, ExtensionPoint {

	protected static SnQApiGateway CsServer;

	public static SnQApiGateway getCsServer() {
		return CsServer;
	}

	public static DescriptorExtensionList<TSBuildStep, CSBuildStepDescriptor> all() {
		return Jenkins.getInstance().getDescriptorList(TSBuildStep.class);
	}

	public abstract boolean perform(final AbstractBuild<?, ?> build, final Launcher launcher, final BuildListener listener, TsServerDetails server)
			throws Exception;

	public CSBuildStepDescriptor getDescriptor() {
		return (CSBuildStepDescriptor)Jenkins.getInstance().getDescriptor(getClass());
	}

	public static abstract class CSBuildStepDescriptor extends Descriptor<TSBuildStep> {

		protected CSBuildStepDescriptor() { }

		protected CSBuildStepDescriptor(Class<? extends TSBuildStep> clazz) {
			super(clazz);
		}
	}
}
