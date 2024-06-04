package com.example.project_ui.RoomDataBase.Plan;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.room.TypeConverters;
import com.example.project_ui.RoomDataBase.Plan.Converters;

import java.util.ArrayList;

@Entity(tableName = "PlanEvents")
public class PlanEvents {
    @PrimaryKey
    @NonNull
    public String date;

    private String event;
    public PlanEvents(String date, String event) {
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
