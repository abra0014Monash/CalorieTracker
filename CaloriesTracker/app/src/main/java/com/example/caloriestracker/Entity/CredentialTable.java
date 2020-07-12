package com.example.caloriestracker.Entity;

import java.util.Date;

public class CredentialTable {
    private String username;
    private String passwordhash;
    private Date signupdate;
    private UserTable userid;

    public CredentialTable() {
    }

    public CredentialTable(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public Date getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(Date signupdate) {
        this.signupdate = signupdate;
    }

    public UserTable getUserid() {
        return userid;
    }

    public void setUserid(UserTable userid) {
        this.userid = userid;
    }
}
