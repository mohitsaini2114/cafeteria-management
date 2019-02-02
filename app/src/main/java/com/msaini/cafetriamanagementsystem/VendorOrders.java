package com.msaini.cafetriamanagementsystem;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VendorOrders extends AppCompatActivity implements RecycleViewVendorOrder.TaskRecycler{
    Firebase firebase;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    NotificationCompat.Builder notification;
    private static final int uniqueId = 12345;

    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    FinalOrder order ;
    ArrayList<FinalOrder> ordersList = new ArrayList<>();
   /* ArrayList<Order> taskCompleted = new ArrayList<>();
    ArrayList<Order> taskPending = new ArrayList<>();*/

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_orders);
       // ((TextView)findViewById(R.id.noOrderVendor)).setVisibility(View.GONE);

        notification = new NotificationCompat.Builder(this, "default");
        notification.setAutoCancel(false);
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
                        if(order.isCompleted){

                           /* ((TextView)findViewById(R.id.noOrderVendor)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.noOrderVendor)).setText("No Orders to display.");*/


                        }else{
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

            findViewById(R.id.manageFood).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VendorOrders.this, AddNewFoodVendor.class);
                    startActivity(intent);
                }
            });


        mRecyclerView = findViewById(R.id.vendorOrderList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecycleViewVendorOrder(ordersList,VendorOrders.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateIsDone(FinalOrder task) {

        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("finalOrder").child(task.userName);
        Map<String,Object> updateObject = new HashMap<>();
        updateObject.put("isCompleted",task.isCompleted);
        r.updateChildren(updateObject);

        mAdapter.notifyDataSetChanged();

        DatabaseReference r1 = FirebaseDatabase.getInstance().getReference().child("order").child(task.userName);
        r1.removeValue();



      /*  notification.setSmallIcon(R.drawable.ic_notification);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification);
        notification.setLargeIcon(bitmap);
        notification.setTicker("This is title");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Hi!");
        notification.setContentText("Your Order has been Prepared");

        Intent intent = new Intent(this, VendorOrders.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(uniqueId, notification.build());*/

        sendSmsBySIntent();

    }
    public void sendSmsBySIntent() {
        // add the phone number in the data
        Uri uri = Uri.parse("smsto:" + "7044212011");

        Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
        // add the message at the sms_body extra field
        smsSIntent.putExtra("sms_body", "Hi! Your order has been prepared. Please come and collect.");
        try{
            startActivity(smsSIntent);
        } catch (Exception ex) {
            Toast.makeText(VendorOrders.this, "Your sms has failed...",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
   /* protected void sendSMSMessage() {
     *//*   phoneNo = txtphoneNo.getText().toString();
        message = txtMessage.getText().toString();*//*

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+17044215468", null, "HII", null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }


 /*   public void notifyUser(View view){
        notification.setSmallIcon(R.drawable.ic_notification);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification);
        notification.setLargeIcon(bitmap);
        notification.setTicker("This is title");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Hi!");
        notification.setContentText("Your Order has been Prepared");

        Intent intent = new Intent(this, VendorOrders.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(uniqueId, notification.build());

    }*/
    @Override
    public void deleteTask(final FinalOrder taskData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(VendorOrders.this);
        builder.setTitle("Delete Task")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("finalOrder").child(taskData.userName);
                        r.removeValue();
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(VendorOrders.this, ""+taskData.orderKey+ " has been deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public int showOption() {
        return 0;
    }
}
