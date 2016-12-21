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

import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector;
import org.jenkinsci.plugins.cloudshell.TSBuildStep;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import org.jenkinsci.plugins.cloudshell.Loggers.QsJenkinsTaskLogger;
import org.jenkinsci.plugins.cloudshell.SnQ_manager.SnQApiGateway;
import org.jenkinsci.plugins.cloudshell.Strcture.JobsDetails;
import org.jenkinsci.plugins.cloudshell.Strcture.SuiteDetails;
import org.kohsuke.stapler.DataBoundConstructor;
import org.jenkinsci.plugins.cloudshell.SnQ_manager.TsServerDetails;
import org.jenkinsci.plugins.cloudshell.Strcture.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

public class EnqueueCustomSuite extends TSBuildStep
{
	private final String suiteName;
	private List<String> list = new List<String>() {		@Override
		public int size() {			return 0;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean contains(Object o) {
			return false;
		}

		@Override
		public Iterator<String> iterator() {
			return null;
		}

		@Override
		public Object[] toArray() {
			return new Object[0];
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return null;
		}

		@Override
		public boolean add(String s) {
			return false;
		}

		@Override
		public boolean remove(Object o) {
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return false;
		}

		@Override
		public boolean addAll(Collection<? extends String> c) {
			return false;
		}

		@Override
		public boolean addAll(int index, Collection<? extends String> c) {
			return false;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return false;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return false;
		}

		@Override
		public void clear() {

		}

		@Override
		public String get(int index) {
			return null;
		}

		@Override
		public String set(int index, String element) {
			return null;
		}

		@Override
		public void add(int index, String element) {

		}

		@Override
		public String remove(int index) {
			return null;
		}

		@Override
		public int indexOf(Object o) {
			return 0;
		}

		@Override
		public int lastIndexOf(Object o) {
			return 0;
		}

		@Override
		public ListIterator<String> listIterator() {
			return null;
		}

		@Override
		public ListIterator<String> listIterator(int index) {
			return null;
		}

		@Override
		public List<String> subList(int fromIndex, int toIndex) {
			return null;
		}
	};

	@DataBoundConstructor
	public EnqueueCustomSuite(String suiteName)
	{
		this.suiteName = suiteName;
	}

	public boolean perform(final AbstractBuild<?, ?> build, final Launcher launcher, final BuildListener listener, TsServerDetails server) throws Exception {
		QsJenkinsTaskLogger logger = new QsJenkinsTaskLogger(listener);
		SnQApiGateway gateway = new SnQApiGateway(logger,server);
		SuiteDetails suiteDetails = null;
		boolean suiteResult = false;

		if (suiteName.contains(" "))
		{
			throw new IOException("\nSuite name cannot contain spaces\n");
		}

		String suiteJSON = gateway.GetSuiteDetails(suiteName);

		if (!suiteJSON.isEmpty())
		{
			suiteDetails = gateway.EnqueuSuite(suiteName,suiteJSON);
		}
		listener.getLogger().println("Suite execution ended. Suite result: " + suiteDetails.SuiteResult);

		printJobs(suiteDetails, listener, server);

		if(suiteDetails.SuiteResult.equals("Succeeded"))
		{
			suiteResult = true;
		}

		return suiteResult;
	}
	
	private void printJobs(SuiteDetails suiteDetails, BuildListener listener, TsServerDetails serverDetails)
	{
		for (JobsDetails job: suiteDetails.JobsDetails)
		{
			listener.getLogger().println("\nJob: "+ job.Name +" result: "+ job.JobResult );
			for(Test test: job.Tests)
			{

				listener.getLogger().println("Test: "+ test.TestPath+" result: "+ test.Result );
				listener.getLogger().println("Test Report: http://"+serverDetails.serverAddress +"/Test/Report?reportId="+test.ReportId);
				listener.getLogger().println("");
			}
		}

	}


    @Extension
	public static final class enqueueCustomSuiteDescriptor extends CSBuildStepDescriptor{

		public enqueueCustomSuiteDescriptor() {
			load();
		}

//		public ListBoxModel doFillGoalTypeItems()throws Exception
//		{
//			ListBoxModel items = new ListBoxModel();
//
//			List<String> suitesNames = getCsServer().getSuitesDetails();
//			for (String suiteName:suitesNames)
//			{
//				items.add(suiteName);
//			}
//			return items;
//		}

		@Override
		public String getDisplayName() {
			return "Enqueue custom suite";
		}

	}	
}