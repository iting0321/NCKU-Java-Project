package com.example.myapplicationtest20240522.RoomDataBase.Plan;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlanDao {
    String tableName = "PlanEvents";

    // insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(PlanEvents planEvents);

    // query
    @Query("SELECT * FROM " + tableName)
    List<PlanEvents> getAll();
    @Query("SELECT * FROM " + tableName + " WHERE id = :id")
    PlanEvents getById(int id);

    // update
    @Query("UPDATE " + tableName + " SET id = :id")
    void updateById(int id);

    // delete
    @Query("DELETE  FROM " + tableName + " WHERE id = :id")
    void deleteData(int id);

}
