package com.example.myapplicationtest20240522.RoomDataBase.Record;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TodoEvents")
public class RecordEvents {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String event;

    public RecordEvents(String event) {
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
