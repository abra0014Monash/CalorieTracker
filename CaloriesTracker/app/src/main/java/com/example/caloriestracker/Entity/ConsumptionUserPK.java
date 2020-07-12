package com.example.caloriestracker.Entity;

import java.util.Date;

public class ConsumptionUserPK {

    private int userid;
    private Date date;
    private int foodid;

    public ConsumptionUserPK() {
    }

    public ConsumptionUserPK(int userid, Date date, int foodid) {
        this.userid = userid;
        this.date = date;
        this.foodid = foodid;
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

    public int getFoodid() {
        return foodid;
    }

    public void setFoodid(int foodid) {
        this.foodid = foodid;
    }
}
