package com.amisha.chitchat;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<ChatMessage> arrayList;
    Activity activity;


    public CustomAdapter(Activity activity, ArrayList<ChatMessage> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mylayout = activity.getLayoutInflater().inflate(R.layout.custom_layout,parent,false);
        MyViewHolder holder=new MyViewHolder(mylayout);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatMessage chatMessage=arrayList.get(position);
        holder.t1.setText(""+chatMessage.getName());
        holder.t2.setText(""+chatMessage.getMessage());


    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView t1, t2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.name);
            t2 = itemView.findViewById(R.id.message);
            img = itemView.findViewById(R.id.imageView);

        }
    }
}
