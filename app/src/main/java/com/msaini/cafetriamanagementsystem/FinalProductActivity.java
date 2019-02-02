package com.msaini.cafetriamanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class FinalProductActivity extends AppCompatActivity {

    FoodList foodList;
    int mQuantity;
    TextView quantity;
    DatabaseReference databaseReference;
    ArrayList<Order> orderArrayList = new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Bitmap BITMAP;
    String foodImageUrl;
    StudentDetails user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_product);

        findViewById(R.id.continueShoppingButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.checkOutShopping).setVisibility(View.INVISIBLE);

        if (getIntent() != null) {
            foodList = (FoodList) getIntent().getSerializableExtra("FOOD_LIST");

            ((TextView) findViewById(R.id.finalPriceText)).setText(foodList.getPrice());
            ((TextView) findViewById(R.id.finalTextDesc)).setText(foodList.getDescription());

            foodImageUrl = foodList.getImageUrl();
            final ImageView imageView = (ImageView) findViewById(R.id.finalFoodImage);

            SharedPreferences sharedPref = this.getSharedPreferences(
                    "myprefs", Context.MODE_PRIVATE);

            //token = sharedPref.getString("token",null);

            String userString = sharedPref.getString("user",null);

            Gson gson = new Gson();

            user = gson.fromJson(userString, StudentDetails.class);

            StorageReference storageRef =  storage.getReferenceFromUrl(foodList.imageUrl);

            final File localFile;
            try {
                localFile = File.createTempFile("images", "jpg");

                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        BITMAP = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imageView.setImageBitmap(BITMAP);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
            }catch (IOException e) {
                e.printStackTrace();
            }
            quantity = findViewById(R.id.quanitiyTextInc);
            quantity.setText("1");

            findViewById(R.id.minusButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int mQuantity = Integer.valueOf(quantity.getText().toString());

                            if(mQuantity > 1){
                                mQuantity -= 1;
                            }

                            quantity.setText(Integer.toString(mQuantity));

                }
            });

            findViewById(R.id.plusButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int mQuantity = Integer.valueOf(quantity.getText().toString());


                    if(mQuantity >= 1 && mQuantity<=6){
                        mQuantity += 1;
                    }
                    // p=mQuantity;
                    quantity.setText(Integer.toString(mQuantity));

                /*    if (Integer.parseInt(quantity.getText().toString())!=1){
                        int quan = Integer.parseInt(quantity.getText().toString());
                        quan = quan+1;
                        quantity.setText(""+quan);
                    }*/

                }
            });

            findViewById(R.id.addToCart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    findViewById(R.id.addToCart).setVisibility(View.INVISIBLE);
                    findViewById(R.id.minusButton).setVisibility(View.INVISIBLE);
                    findViewById(R.id.plusButton).setVisibility(View.INVISIBLE);
                    findViewById(R.id.quanitiyTextInc).setVisibility(View.INVISIBLE);
                    findViewById(R.id.qunatityTextView).setVisibility(View.INVISIBLE);

                    findViewById(R.id.checkOutShopping).setVisibility(View.VISIBLE);
                    findViewById(R.id.continueShoppingButton).setVisibility(View.VISIBLE);

                    String price = ((TextView) findViewById(R.id.finalPriceText)).getText().toString();
                    String description = ((TextView) findViewById(R.id.finalTextDesc)).getText().toString();
                    String quantity = ((TextView) findViewById(R.id.quanitiyTextInc)).getText().toString();

                    Order order = new Order(description, price,quantity, new Date().toString(),false);
                    order.imageUrl = foodImageUrl;



                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    //databaseReference.child("order").child(user.name).push().setValue(order);
                    databaseReference.child("order").child(user.name).child(order.description).setValue(order);

                }
            });

            findViewById(R.id.continueShoppingButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FinalProductActivity.this, FoodListActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            findViewById(R.id.checkOutShopping).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(FinalProductActivity.this, OrderSummaryActivity.class);
                    intent.putExtra("orderList", orderArrayList);
                    startActivity(intent);
                    finish();

                }
            });

        }

        }
}

