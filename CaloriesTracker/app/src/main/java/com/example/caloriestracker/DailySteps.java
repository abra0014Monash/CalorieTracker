package com.example.caloriestracker;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class DailySteps {

    @PrimaryKey(autoGenerate = true)
    public int userid;
    @ColumnInfo(name = "steps_taken")
    public int steps_taken;
    @ColumnInfo(name = "step_date")
    public String step_date;


    public DailySteps(int steps_taken,String step_date) {
       this.steps_taken = steps_taken;
       this.step_date = step_date;
    }

    public int getId() {
        return userid;
    }

    public int getStepsTaken() {
        return steps_taken;
    }

    public void setStepsTaken(int steps_taken) {
        this.steps_taken = steps_taken;
    }

    public String getDate() {
        return step_date;
    }

    public void setDate(String step_date) {
        this.step_date = step_date;
    }

}