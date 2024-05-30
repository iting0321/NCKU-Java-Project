package com.example.myapplicationtest20240522.RoomDataBase.Record;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordDao {
    String tableName = "TodoEvents";

    // insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(RecordEvents recordEvents);

    // query
    @Query("SELECT * FROM " + tableName)
    List<RecordEvents> getAll();
    @Query("SELECT * FROM " + tableName + " WHERE id = :id")
    RecordEvents getById(int id);

    // update
    @Query("UPDATE " + tableName + " SET id = :id")
    void updateById(int id);

    // delete
    @Query("DELETE  FROM " + tableName + " WHERE id = :id")
    void deleteData(int id);

}
