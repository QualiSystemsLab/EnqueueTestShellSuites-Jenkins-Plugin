package org.jenkinsci.plugins.cloudshell.Strcture;

import jnr.ffi.Struct;

import java.util.List;

/**
 * Created by aharon.s on 12/18/2016.
 */
public class JobsDetails
{
    public String Id ;
    public String OwnerName ;
    public String JobState ;
    public String JobResult ;
    public String JobFailureDescription ;
    public String EnqueueTime ;
    public String StartTime ;
    public String EndTime ;
    public double ElapsedTime ;
    public boolean UseAnyExecutionServer ;
    public String SelectedExecutionServer ;
    public String SuiteId ;
    public String ExpectedStartTime ;
    public String Name ;
    public String Description ;
    public List<String> ExecutionServers ;
    public String LoggingProfile ;
    public double EstimatedDuration ;
    public boolean StopOnFail ;
    public boolean StopOnError ;
    public List<Test> Tests ;
    public Topology Topology ;
    public double DurationTimeBuffer ;
    public String EmailNotifications ;
    public String Type ;
}
