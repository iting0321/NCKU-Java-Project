package com.example.project_ui.RoomDataBase.Todo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TodoEvents.class}, version = 1)
public abstract class


DataBase extends RoomDatabase {
    public abstract TodoDao todoDao();
    public static final String DB_NAME = "TodoData.db";//資料庫名稱
}
