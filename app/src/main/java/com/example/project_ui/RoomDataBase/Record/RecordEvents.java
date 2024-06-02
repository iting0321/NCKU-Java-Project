package com.example.project_ui.RoomDataBase.Record;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "RecordEvents")
public class RecordEvents {
    @PrimaryKey
    @NonNull
    public String date;

    public String event;
    public RecordEvents(String date, String event) {
        this.date = date;
        this.event = event;
    }
    public String getDate() {
        return date;
    }
    public String getEvent() {
        return event;
    }
    public void setEvent(String newEvent){
        this.event = newEvent;
    }
}
