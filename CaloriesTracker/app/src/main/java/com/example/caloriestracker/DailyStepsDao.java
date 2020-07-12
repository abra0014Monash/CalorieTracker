package com.example.caloriestracker;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DailyStepsDao {
    @Query("SELECT * FROM dailysteps")
    List<DailySteps> getAll();

    //@Query("SELECT * FROM DailySteps WHERE step_date LIKE :first AND " + "last_name LIKE :last LIMIT 1")
    @Query("SELECT * FROM dailySteps WHERE step_date = :step_date LIMIT 1")
    List<DailySteps> findByDate(String step_date);

    @Query("SELECT * FROM dailysteps WHERE userid = :userid LIMIT 1")
    DailySteps findByID(int userid);

    @Insert
    void insertAll(DailySteps... dailySteps);

    @Insert
    long insert(DailySteps dailySteps);

//    @Delete
//    void delete(DailySteps dailySteps);

    @Update(onConflict = REPLACE)
    public void updateSteps(DailySteps... dailySteps);
//
//    @Query("DELETE FROM dailysteps")
//    void deleteAll();
}