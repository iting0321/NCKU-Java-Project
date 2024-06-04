package com.example.project_ui.RoomDataBase.Schedule;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ScheduleEvents.class}, version = 1)
public abstract class


DataBase extends RoomDatabase {
    public abstract ScheduleDao scheduleDao();
    public static final String DB_NAME = "ScheduleData.db";//資料庫名稱
}
