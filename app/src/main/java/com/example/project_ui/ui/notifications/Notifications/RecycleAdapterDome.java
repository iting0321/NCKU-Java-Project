package com.example.project_ui.ui.notifications.Notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_ui.R;

import java.util.List;

public class RecycleAdapterDome extends RecyclerView.Adapter<RecycleAdapterDome.MyViewHolder> {
    private Context context;
    private List<String> list;
    private View inflater;

    public RecycleAdapterDome(Context context,List<String> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        inflater= LayoutInflater.from(context).inflate(R.layout.item_dome,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,int position){
        holder.textview.setText(list.get(position));
    }
    public int getItemCount(){
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textview;
        public MyViewHolder(View itemview){
            super(itemview);
            textview=(TextView) itemview.findViewById(R.id.text_view);
        }

    }



}
