package com.example.project_ui.RoomDataBase.Plan;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PlanDao {
    String tableName = "PlanEvents";

    // insert
    @Query("INSERT INTO "+tableName+"(date,event) VALUES(:date,:events)")
    void insertData(String date, String events);

    // query
    @Query("SELECT * FROM " + tableName)
    List<PlanEvents> getAll();
    @Query("SELECT * FROM " + tableName + " WHERE date = :date")
    PlanEvents getByDate(String date);

    // update
    @Query("UPDATE "+tableName+" SET event = :event WHERE date = :date")
    void updateByDate(String date, String event);

    // delete
    @Query("DELETE  FROM " + tableName + " WHERE date = :date")
    void deleteData(String date);

}
