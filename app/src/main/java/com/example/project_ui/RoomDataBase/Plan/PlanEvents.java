package com.example.myapplicationtest20240522.RoomDataBase.Plan;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PlanEvents")
public class PlanEvents {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String event;

    public PlanEvents(String event) {
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
