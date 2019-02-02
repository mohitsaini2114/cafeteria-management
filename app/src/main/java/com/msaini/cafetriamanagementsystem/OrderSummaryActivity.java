package com.msaini.cafetriamanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class OrderSummaryActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    private ArrayList<Order> orderArrayList = new ArrayList<>();
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    public double totalPrice =0.0;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Bitmap BITMAP;
    StudentDetails user;
    double ninerWallet;
    StringBuilder buffer = new StringBuilder("");
    StringBuilder bufferQuantity = new StringBuilder();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        SharedPreferences sharedPref = this.getSharedPreferences(
                "myprefs", Context.MODE_PRIVATE);

        //token = sharedPref.getString("token",null);

        String userString = sharedPref.getString("user",null);

        Gson gson = new Gson();
        user = gson.fromJson(userString, StudentDetails.class);
        ninerWallet = Double.parseDouble(user.getNinerWallet());

       getOrderData();






        mRecyclerView = findViewById(R.id.orderList);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new MyAdapter(orderArrayList);
        mRecyclerView.setAdapter(mAdapter);



            findViewById(R.id.PayNowButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (totalPrice>0.0) {
                        if (ninerWallet>= totalPrice) {


                            Random rand = new Random();

                            // Generate random integers in range 0 to 999
                            int rand_int1 = rand.nextInt(10000);

                            FinalOrder finalOrder = new FinalOrder(user.name, String.valueOf(rand_int1), false, new Date().toString(), Double.toString(totalPrice));
                            finalOrder.foodItems = buffer.toString();
                            finalOrder.foodQuant = bufferQuantity.toString();
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("finalOrder").child(user.name).setValue(finalOrder);

                            //finalOrder.totalAmount = totalPrice;


                            Intent intent = new Intent(OrderSummaryActivity.this, FinalTransaction.class);
                            intent.putExtra("final_amount", finalOrder);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(OrderSummaryActivity.this, FinalTransaction.class);
                            //intent.putExtra("final_amount", finalOrder);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        Toast.makeText(OrderSummaryActivity.this, "Please add some items in cart to proceed further.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        ArrayList<Order> orderList;
        Order list;

        public MyAdapter(ArrayList<Order> messageList) {
            this.orderList = messageList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_custom, parent, false);
           MyViewHolder vh = new MyViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            list = orderList.get(position);


            holder.foodName.setText(list.getDescription());
            //holder.foodPrice.setText(list.getPrice());
            holder.foodQuantity.setText(list.getQuantity());

            StorageReference storageRef =  storage.getReferenceFromUrl(list.imageUrl);

            final File localFile;
            try {
                localFile = File.createTempFile("images", "jpg");

                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        BITMAP = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        holder.orderImage.setImageBitmap(BITMAP);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
           /* int qty = Integer.parseInt(list.getQuantity());
            double price = Double.parseDouble(list.getPrice());
            totalPrice = totalPrice+ (qty*price);*/

            holder.deleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getPosition();

                   list =  orderArrayList.get(position);
                   deleteTask(list);
                    Toast.makeText(OrderSummaryActivity.this, ""+list.getDescription()+ " has been deleted", Toast.LENGTH_SHORT).show();
                }
            });

        }
        @Override
        public int getItemCount() {
            return orderList.size();

        }


        public  class MyViewHolder extends RecyclerView.ViewHolder {

            TextView foodName;
           // TextView foodPrice;
            TextView foodQuantity;
            ImageView deleteOrder;
            ImageView orderImage;
            ProgressBar progressBar;

            public MyViewHolder(View v) {
                super(v);
                this.foodName = v.findViewById(R.id.itemNameText);
                //this.foodPrice = v.findViewById(R.id.foodPrice);
                this.foodQuantity = v.findViewById(R.id.foodQuantity);
                this.deleteOrder  = v.findViewById(R.id.orderDelete);
                this.orderImage = v.findViewById(R.id.order_image);
                this.progressBar = v.findViewById(R.id.image_loader);

            }
        }

    }

    public void getOrderData(){
        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.child("order/").child(user.name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orderArrayList.clear();
                totalPrice = 0.0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Order orderData = (Order) snapshot.getValue(Order.class);

                    orderData.orderID = snapshot.getKey();
                    //orderData.user = snapshot.getKey();

                    orderArrayList.add(orderData);
                    int qty = Integer.parseInt(orderData.getQuantity());
                    double price = Double.parseDouble(orderData.getPrice());
                    totalPrice = totalPrice+ (qty*price);
                    buffer.append(orderData.description);
                    buffer.append(",");
                    bufferQuantity.append(orderData.quantity);
                    bufferQuantity.append(",");


                }
                mAdapter.notifyDataSetChanged();
                ((TextView)findViewById(R.id.totalPrice)).setText(totalPrice+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deleteTask(Order list){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("order/" + user.name+"/"+list.orderID);
        //Query taskQuery = ref.child("order/" + list.getOrderID());
        ref.removeValue();
        mAdapter.notifyDataSetChanged();


    }
}
