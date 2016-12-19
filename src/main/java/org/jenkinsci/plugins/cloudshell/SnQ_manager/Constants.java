package org.jenkinsci.plugins.cloudshell.SnQ_manager;

/**
 * SnQ
 * Created by aharon.s on 12/5/2016.
 */
public class Constants {
    public static String SnQDefaultport = "9000";
    public static String SUITES_URI = "/Scheduling/SuiteTemplates";
    public static String SELECTED_SUITE_URI = "/Scheduling/SuiteTemplates/";
    public static String ENQUEUE_SUITE = "/Scheduling/Suites";
    public static String GET_SUITE_DETAILS = "/Scheduling/Suites/";
    public static String GET_JOB_DETAILS = "/Scheduling/Jobs/";
    public static int POOLING_TIME_SECONDS_INTERVAL = 30;



    public static String BLUEPRINT_CONFLICT_ERROR = "Blueprint has conflicting resources";
    public static String ERROR_CATEGORY = "errorCategory";
    public static String MESSAGE = "message";
    public static String SANDBOXES_URI = "/sandboxes/";
    public static String BLUEPRINTS_URI = "/blueprints/";
    public static String API_VERSION = "/v1";

    public Constants() {
    }
}



