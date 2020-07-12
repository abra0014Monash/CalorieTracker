package com.example.caloriestracker;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {DailySteps.class}, version = 2, exportSchema = false)
public abstract class DailyStepsDatabase extends RoomDatabase {
    public abstract DailyStepsDao dailyStepsDao();
    private static volatile DailyStepsDatabase INSTANCE;
    static DailyStepsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DailyStepsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    DailyStepsDatabase.class, "dailysteps_database")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}