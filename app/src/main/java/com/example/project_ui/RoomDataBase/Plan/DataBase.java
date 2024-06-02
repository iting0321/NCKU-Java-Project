package com.example.project_ui.RoomDataBase.Plan;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import java.util.ArrayList;

@Database(entities = {PlanEvents.class}, version = 1)
public abstract class


DataBase extends RoomDatabase {
    public abstract PlanDao planDao();
    public static final String DB_NAME = "PlanData.db";//資料庫名稱
}
