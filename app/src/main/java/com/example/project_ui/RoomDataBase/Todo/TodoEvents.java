package com.example.project_ui.RoomDataBase.Todo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TodoEvents")
public class TodoEvents {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String event;

    public TodoEvents(String event) {
        this.event = event;
    }
    public int getId() {
        return id;
    }
    public String getEvent() {
        return event;
    }
    public void setEvent(String newEvent){
        this.event = newEvent;
    }
}
