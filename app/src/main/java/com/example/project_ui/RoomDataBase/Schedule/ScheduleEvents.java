package com.example.project_ui.RoomDataBase.Schedule;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "ScheduleEvents")
public class ScheduleEvents {
    @PrimaryKey
    @NonNull
    public String date;

    private String event;
    public ScheduleEvents(String date, String event) {
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
