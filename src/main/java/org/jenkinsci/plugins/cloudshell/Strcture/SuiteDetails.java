package org.jenkinsci.plugins.cloudshell.Strcture;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by aharon.s on 12/18/2016.
 */
public class SuiteDetails
{
    public String SuiteId ;
    public String SuiteName ;
    public String SuiteTemplateName ;
    public String Description ;
    public String Owner ;
    public String SuiteStatus ;
    public String SuiteResult ;
    public int RemainingJobs ;
    public String StartTime ;
    public String EndTime ;
    public String Type ;
    public double RemoveJobsFromQueueAfter ;
    public boolean EndReservationOnEnd ;
    public List<JobsDetails> JobsDetails ;
    public String EmailNotifications ;

}
