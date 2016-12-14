package org.jenkinsci.plugins.cloudshell.Loggers;

/**
 * SnQ
 * Created by aharon.s on 12/5/2016.
 */
public abstract class QsLogger {
    public QsLogger() {
    }

    public abstract void Debug(String var1);

    public abstract void Info(String var1);

    public abstract void Error(String var1);
}
