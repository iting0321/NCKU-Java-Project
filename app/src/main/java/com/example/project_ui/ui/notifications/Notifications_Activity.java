package com.example.project_ui.ui.notifications;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ui.R;

public class Notifications_Activity extends AppCompatActivity {
    private RecyclerView tasksRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notifications);
        getSupportActionBar().hide();

        tasksRecyclerView= findViewById(R.id.tasksRecyclersView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
