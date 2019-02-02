package com.msaini.cafetriamanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FoodListActivity extends AppCompatActivity {


    FirebaseStorage storage = FirebaseStorage.getInstance();
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    Bitmap BITMAP;
    Firebase firebase;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ChildEventListener childEventListener;
    String key = null;
    private ArrayList<FoodList> foodListArrayList = new ArrayList<>();

    FoodList foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        if (isConnected()) {

          /*  ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };*/


            databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child("Food/Bojang").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //foodListArrayList.clear();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                            FoodList taskData = (FoodList) snapshot.getValue(FoodList.class);
                            Log.d("Demo", taskData.toString());
                            taskData.key = snapshot.getKey();
                            foodListArrayList.add(taskData);

                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                mRecyclerView = findViewById(R.id.recycleMessagesList);
                mRecyclerView.setHasFixedSize(true);

                //mLayoutManager = new LinearLayoutManager(this);
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                //mRecyclerView.setLayoutManager(mLayoutManager);


                mAdapter = new MyAdapter(foodListArrayList);
                mRecyclerView.setAdapter(mAdapter);


            }

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        ArrayList<FoodList> foodArrayList;
        FoodList list;

        public MyAdapter(ArrayList<FoodList> messageList) {
            this.foodArrayList = messageList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_custom, parent, false);
            MyViewHolder vh = new MyViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

             list = foodArrayList.get(position);


            holder.foodPrice.setText("Price " +list.getPrice()+" $");
            holder.foodDescription.setText(list.getDescription());

            StorageReference storageRef =  storage.getReferenceFromUrl(list.imageUrl);

            final File localFile;
            try {
                localFile = File.createTempFile("images", "jpg");

            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    BITMAP = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.foodImage.setImageBitmap(BITMAP);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
            } catch (IOException e) {
                e.printStackTrace();
            }
           /* switch (list.getImage())
            {
                case "burger":holder.foodImage.setImageResource(R.drawable.burger); break;
                case "wrap":holder.foodImage.setImageResource(R.drawable.wrap); break;
                case "pizza":holder.foodImage.setImageResource(R.drawable.pizza); break;

            }*/
            holder.foodImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FoodList foodList = foodListArrayList.get(position);
                    Intent intent = new Intent(FoodListActivity.this, FinalProductActivity.class);
                    intent.putExtra("FOOD_LIST", foodList);
                    startActivity(intent);
                }
            });

        }
        @Override
        public int getItemCount() {
            return foodArrayList.size();

        }


        public  class MyViewHolder extends RecyclerView.ViewHolder {

            TextView foodPrice;
            TextView foodDescription;
            ImageView foodImage;

            public MyViewHolder(View v) {
                super(v);
                this.foodPrice = v.findViewById(R.id.foodPrice);
                this.foodDescription = v.findViewById(R.id.foodDesList);

                this.foodImage = v.findViewById(R.id.foodImage);



                foodImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });

            }
        }

    }
}
