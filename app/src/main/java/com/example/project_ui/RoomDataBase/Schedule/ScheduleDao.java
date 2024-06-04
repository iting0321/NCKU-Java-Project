package com.example.project_ui.RoomDataBase.Schedule;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScheduleDao {
    String tableName = "ScheduleEvents";

    // insert
    @Query("INSERT INTO "+tableName+"(date,event) VALUES(:date,:events)")
    void insertData(String date, String events);

    // query
    @Query("SELECT * FROM " + tableName)
    List<ScheduleEvents> getAll();
    @Query("SELECT * FROM " + tableName + " WHERE date = :date")
    ScheduleEvents getByDate(String date);

    // update
    @Query("UPDATE "+tableName+" SET event = :event WHERE date = :date")
    void updateByDate(String date, String event);

    // delete
    @Query("DELETE  FROM " + tableName + " WHERE date = :date")
    void deleteData(String date);

}
