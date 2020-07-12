package com.example.caloriestracker.Entity;

import java.math.BigDecimal;
import java.util.Date;

public class Report {

    private BigDecimal totalcalorieconsumed;
    private BigDecimal totalcalorieburned;
    private Integer totalstepstaken;
    private BigDecimal caloriegoal;
    private UserTable userTable;
    protected ReportPK reportPK;

    public Report() {
    }

    public Report(ReportPK reportPK) {
        this.reportPK = reportPK;
    }

    public Report(int userid, Date date) {
        this.reportPK = new ReportPK(userid, date);
    }

    public ReportPK getReportPK() {
        return reportPK;
    }

    public void setReportPK(ReportPK reportPK) {
        this.reportPK = reportPK;
    }

    public BigDecimal getTotalcalorieconsumed() {
        return totalcalorieconsumed;
    }

    public void setTotalcalorieconsumed(BigDecimal totalcalorieconsumed) {
        this.totalcalorieconsumed = totalcalorieconsumed;
    }

    public BigDecimal getTotalcalorieburned() {
        return totalcalorieburned;
    }

    public void setTotalcalorieburned(BigDecimal totalcalorieburned) {
        this.totalcalorieburned = totalcalorieburned;
    }

    public Integer getTotalstepstaken() {
        return totalstepstaken;
    }

    public void setTotalstepstaken(Integer totalstepstaken) {
        this.totalstepstaken = totalstepstaken;
    }

    public BigDecimal getCaloriegoal() {
        return caloriegoal;
    }

    public void setCaloriegoal(BigDecimal caloriegoal) {
        this.caloriegoal = caloriegoal;
    }

    public UserTable getUserTable() {
        return userTable;
    }

    public void setUserTable(UserTable userTable) {
        this.userTable = userTable;
    }
}
