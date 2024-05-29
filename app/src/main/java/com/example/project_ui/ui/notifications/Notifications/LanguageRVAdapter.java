package com.example.project_ui.ui.notifications.Notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ui.R;

import java.util.ArrayList;

public class LanguageRVAdapter extends RecyclerView.Adapter<LanguageRVAdapter.ViewHolder> {
    private ArrayList<String> languageRVModalArrayList;
    public LanguageRVAdapter(ArrayList<String> languageRVModalArrayList){
        this.languageRVModalArrayList = languageRVModalArrayList;
    }
    @Override
    public LanguageRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
//        inflater= LayoutInflater.from(context).inflate(R.layout.item_dome,parent,false);
//        ViewHolder myViewHolder = new ViewHolder(inflater);

        //return myViewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dome, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(LanguageRVAdapter.ViewHolder holder,int position){
        holder.textview.setText(languageRVModalArrayList.get(position));
    }
    public int getItemCount(){
        return languageRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textview;
        public ViewHolder(View itemview){
            super(itemview);
            textview=(TextView) itemview.findViewById(R.id.idTVLngName);
        }

    }



}
