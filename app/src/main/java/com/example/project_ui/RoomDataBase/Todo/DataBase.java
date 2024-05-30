package com.example.myapplicationtest20240522.RoomDataBase.Todo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TodoEvents.class}, version = 1)
public abstract class


DataBase extends RoomDatabase {
    public abstract TodoDao todoDao();

    public static final String DB_NAME = "TodoData.db";//資料庫名稱
    private static volatile DataBase instance;

    public static synchronized DataBase getInstance(Context context){
        if(instance == null){
            instance = create(context);//創立新的資料庫
        }
        return instance;
    }


    private static DataBase create(final Context context){
        return Room.databaseBuilder(context,DataBase.class,DB_NAME).build();
    }


}
