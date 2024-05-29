package com.example.project_ui.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ui.R;
import com.example.project_ui.databinding.FragmentNotificationsBinding;
import com.example.project_ui.ui.notifications.Notifications.LanguageRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private LanguageRVAdapter lngRVAdapter;
    private EditText addEdt;
    private Button addBtn;
    private ArrayList<String> lngList;


    private FragmentNotificationsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        recyclerView = view.findViewById(R.id.recycler_view);
        addEdt = view.findViewById(R.id.idEdtAdd);
        addBtn = view.findViewById(R.id.idBtnAdd);
        lngList = new ArrayList<>();



        lngRVAdapter = new LanguageRVAdapter(lngList);
        recyclerView.setAdapter(lngRVAdapter);

        // on below line we are adding click listener for our add button.
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are calling
                // add item method.
                addItem(addEdt.getText().toString());

            }
        });
        return view;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void addItem(String item) {
        // on below line we are checking
        // if item is empty or not.
        if (!item.isEmpty()) {
            // on below line we are adding
            // item to our list
            lngList.add(item);

            // on below line we are notifying
            // adapter that data has updated.
            lngRVAdapter.notifyDataSetChanged();
            addEdt.setText("");
        }
    }


}


