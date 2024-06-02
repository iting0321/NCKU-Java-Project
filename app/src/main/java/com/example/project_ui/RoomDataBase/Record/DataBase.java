package com.example.project_ui.RoomDataBase.Record;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.project_ui.RoomDataBase.Record.RecordDao;
import com.example.project_ui.RoomDataBase.Record.RecordEvents;

import java.util.ArrayList;

@Database(entities = {RecordEvents.class}, version = 1)
public abstract class


DataBase extends RoomDatabase {
    public abstract RecordDao recordDao();
    public static final String DB_NAME = "RecordData.db";//資料庫名稱
}

