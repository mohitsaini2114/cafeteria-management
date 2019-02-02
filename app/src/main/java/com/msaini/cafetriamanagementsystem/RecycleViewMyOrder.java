package com.msaini.cafetriamanagementsystem;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

public class RecycleViewMyOrder extends RecyclerView.Adapter<RecycleViewMyOrder.MyViewHolder> {
    ArrayList<FinalOrder> orderListNew;
    FinalOrder list;
    TaskRecycler taskRecycler;
    DatabaseReference databaseReference;
    PrettyTime p;
    public View v;

    public RecycleViewMyOrder(ArrayList<FinalOrder> messageList, TaskRecycler taskRecycler) {
        this.orderListNew = messageList;
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        this.taskRecycler = taskRecycler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_my_orders, parent, false);
        this.v = v;
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        list = orderListNew.get(position);
        p = new PrettyTime();

        holder.desc.setText("Order Number "+list.orderNumber);


        String data = list.foodItems;
        String[] items = data.split(",");
        StringBuilder foodData = new StringBuilder();

        String dataQuant = list.foodQuant;
        String[] itemsQuant = dataQuant.split(",");


        for (int i = 0; i < items.length && i < items.length; i++) {

            foodData.append(items[i]+" - "+itemsQuant[i] + "\n");
        }
        holder.items.setText(foodData.toString());

        holder.dateTimeText.setText(p.format(new Date(list.scheduleTime)));

    }

    @Override
    public int getItemCount() {
        return orderListNew.size();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView desc;
        TextView items;

        TextView dateTimeText;
        ImageView callVendor;

        public MyViewHolder(View v) {
            super(v);
            this.desc = v.findViewById(R.id.desc);
            this.items = v.findViewById(R.id.items);

            this.dateTimeText = v.findViewById(R.id.dateTimeText);
            this.callVendor = v.findViewById(R.id.callVendor);

            v.findViewById(R.id.callVendor).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    taskRecycler.callVendor();


                }
            });
        }
    }


    public interface TaskRecycler {
        void callVendor();


    }


}
