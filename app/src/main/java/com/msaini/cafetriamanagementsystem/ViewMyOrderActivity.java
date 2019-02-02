package com.msaini.cafetriamanagementsystem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewMyOrderActivity extends AppCompatActivity implements RecycleViewMyOrder.TaskRecycler {

    Firebase firebase;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StudentDetails user;

    NotificationCompat.Builder notification;
    private static final int uniqueId = 12345;

    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    FinalOrder order ;
    ArrayList<FinalOrder> ordersList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_order);

        SharedPreferences sharedPref = this.getSharedPreferences(
                "myprefs", Context.MODE_PRIVATE);

        //token = sharedPref.getString("token",null);

        String userString = sharedPref.getString("user",null);

        Gson gson = new Gson();

        user = gson.fromJson(userString, StudentDetails.class);

       /* notification = new NotificationCompat.Builder(this, "default");
        notification.setAutoCancel(false);*/
        firebaseDatabase  = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("finalOrder");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    order = snapshot.getValue(FinalOrder.class);
                    order.userName = snapshot.getKey();
                    //order.orderKey = snapshot.getKey();

                    if(order.userName.equalsIgnoreCase(user.name)) {
                        ordersList.add(order);
                    }


                }

                /* Collections.sort(taskCompleted, new Order(false));
                 *//*if(order.isDone) {
                            ordersList.add(order);
                            *//**//*Order order1 = new Order();
                            order1.compare(order, order );*//**//*
                            Collections.sort(taskCompleted, new Order());
                        }*//*
                 *//*else
                            ordersList.add(order);*//*

                    }
                   *//* Collections.sort(taskCompleted, new Order());
                    Collections.sort(taskPending, new Order());*/
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        mRecyclerView = findViewById(R.id.myOrderList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecycleViewMyOrder(ordersList,ViewMyOrderActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void callVendor() {

        /*Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:7044212011"));

        if (ActivityCompat.checkSelfPermission(ViewMyOrderActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);*/
        String phoneNo = "7044212011";

        String dial = "tel:" + phoneNo;
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }

}
