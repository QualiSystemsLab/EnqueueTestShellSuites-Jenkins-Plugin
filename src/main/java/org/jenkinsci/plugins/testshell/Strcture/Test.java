package org.jenkinsci.plugins.testshell.Strcture;

import java.util.List;

/**
 * Created by aharon.s on 12/18/2016.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Test 
{
    public String TestPath;
    public String State;
    public String StartTime;
    public String EndTime;
    public String Result;
    public String ReportId ;
    public String ReportLink ;
    public List<Parameter> Parameters;
    public String EstimatedDuration;
}
