/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cfiv.simpleweb.dbaccess;

import com.cfiv.simpleweb.common.Configuration;

/**
 *
 * @author tanimura
 */
public class DBParameter {
    private String dbDriver = "";
    private String dbURL = "";
    private String dbUser = "";
    private String dbPassword = "";

    public DBParameter() {
        dbDriver = "com.mysql.jdbc.Driver";
        dbURL = Configuration.getDBURL();
        dbUser = Configuration.getDBUser();
        dbPassword = Configuration.getDBPassword();
    }

    public String getDriver() {
        return dbDriver;
    }

    public String getURL() {
        return dbURL;
    }

    public String getUser() {
        return dbUser;
    }

    public String getPassword() {
        return dbPassword;
    }
}
