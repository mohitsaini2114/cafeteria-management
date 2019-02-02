package com.msaini.cafetriamanagementsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VendorActivity extends AppCompatActivity {

    String userName;
    String password;
    VendorDetails vendorDetails;
    ArrayList<VendorDetails> vendorDetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        VendorDetails vendorDetails = new VendorDetails("msaini@gmail.com", "12345", "Mohit");

        databaseReference.child("vendor").push().setValue(vendorDetails);*/
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("vendor");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ordersList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    vendorDetails = snapshot.getValue(VendorDetails.class);
                    vendorDetails.key = snapshot.getKey();
                    vendorDetailsList.add(vendorDetails);

                           /* ((TextView)findViewById(R.id.noOrderVendor)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.noOrderVendor)).setText("No Orders to display.");*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.vendorLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) findViewById(R.id.vendorId)).getText().toString().isEmpty() || ((EditText) findViewById(R.id.vendorId)).getText().toString().equals("")) {

                    ((EditText) findViewById(R.id.vendorId)).setError("Enter Vendor Id");
                }
              /*  else if(((EditText) findViewById(R.id.text_email)).getText().toString().length() < 9){
                    ((EditText) findViewById(R.id.text_email)).setError("Enter Student Id of 10 digits");

                }*/
                else {
                    userName = ((EditText) findViewById(R.id.vendorId)).getText().toString();
                }

                if (((EditText) findViewById(R.id.vendorPass)).getText().toString().isEmpty() || ((EditText) findViewById(R.id.vendorPass)).getText().toString().equals("")) {

                    ((EditText) findViewById(R.id.vendorPass)).setError("Enter Password");
                } else {
                    password = ((EditText) findViewById(R.id.vendorPass)).getText().toString();

                    for (VendorDetails vendorDetailsNew:vendorDetailsList) {
                        if(vendorDetailsNew.getVendorId().equalsIgnoreCase(userName) && vendorDetailsNew.getPassword().equalsIgnoreCase(password)) {


                            Intent messageThreads = new Intent(VendorActivity.this, VendorOrders.class);
                            startActivity(messageThreads);
                            finish();
                            break;
                        }
                        else{
                            Toast.makeText(VendorActivity.this, "Incorrect User name or Password", Toast.LENGTH_SHORT).show();
                        }
                    }


                }


            }


        });
    }
}
