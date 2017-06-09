package com.example.rest.server.db.manager;

/**
 * Created by Cristina on 5/30/2017.
 */
public class DatabaseInfo {
    private String driver;
    private String url;
    private String user;
    private String password;

    public DatabaseInfo(String driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
