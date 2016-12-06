package org.jenkinsci.plugins.cloudshell.SnQ_manager;

/**
 * SnQ
 * Created by aharon.s on 12/5/2016.
 */
public class TsServerDetails {
    public final String serverAddress;
    public final String user;
    public final String pw;
    public final String domain;
    public final boolean ignoreSSL;

    public TsServerDetails(String serverAddress, String user, String pw, String domain, boolean ignoreSsl) {
        this.serverAddress = serverAddress;
        this.user = user;
        this.pw = pw;
        this.domain = domain;
        this.ignoreSSL = ignoreSsl;
    }
}

