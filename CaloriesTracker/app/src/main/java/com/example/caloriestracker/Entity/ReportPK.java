package com.example.caloriestracker.Entity;

import java.util.Date;

public class ReportPK {

    private int userid;
    private Date date;

    public ReportPK() {
    }

    public ReportPK(int userid, Date date) {
        this.userid = userid;
        this.date = date;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
