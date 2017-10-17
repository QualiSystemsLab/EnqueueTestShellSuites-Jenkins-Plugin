package org.jenkinsci.plugins.cloudshell.Strcture;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.text.DateFormat;
import java.util.List;
import java.util.Objects;

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
