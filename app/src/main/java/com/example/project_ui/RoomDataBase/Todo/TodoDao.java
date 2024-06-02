package com.example.project_ui.RoomDataBase.Todo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TodoDao {
    String tableName = "TodoEvents";

    // insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(TodoEvents todoEvents);

    // query
    @Query("SELECT * FROM " + tableName)
    List<TodoEvents> getAll();
    @Query("SELECT * FROM " + tableName + " WHERE id = :id")
    TodoEvents getById(int id);

    // delete
    @Query("DELETE  FROM " + tableName + " WHERE event = :event")
    void deleteData(String event);

}
