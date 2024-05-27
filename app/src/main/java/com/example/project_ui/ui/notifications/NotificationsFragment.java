package com.example.project_ui.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ui.R;
import com.example.project_ui.databinding.FragmentNotificationsBinding;
import com.example.project_ui.ui.notifications.Notifications.RecycleAdapterDome;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecycleAdapterDome adapterDome;
    private Context context;
    private List<String> list;


    private FragmentNotificationsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        View view=inflater.inflate(R.layout.fragment_notifications,container,false);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycler_view);
        list=new ArrayList<>();
        for(int i=0;i<10;++i){
            list.add("This is test"+i);
        }
        adapterDome=new RecycleAdapterDome(context,list);


        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterDome);
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        //binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();
        //recyclerView=root.findViewById(R.id.recycler_view);
        //run();
        /*final TextView textView = binding.test_line;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return view;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}