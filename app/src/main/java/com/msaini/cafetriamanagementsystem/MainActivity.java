package com.msaini.cafetriamanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Firebase firebase;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ChildEventListener childEventListener;
    StudentDetails studentDetails;
    ArrayList<StudentDetails> studentDetailsList = new ArrayList<>();

    public static String STUDENT_OBJECT = "User Object";
    String userName;
    String password;
    boolean isStudentExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isConnected()) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child("user");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //ordersList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        studentDetails = snapshot.getValue(StudentDetails.class);
                        studentDetails.studentKey = snapshot.getKey();
                        studentDetailsList.add(studentDetails);

                           /* ((TextView)findViewById(R.id.noOrderVendor)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.noOrderVendor)).setText("No Orders to display.");*/
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((EditText) findViewById(R.id.text_email)).getText().toString().isEmpty() || ((EditText) findViewById(R.id.text_email)).getText().toString().equals("")) {

                        ((EditText) findViewById(R.id.text_email)).setError("Enter Student Id");
                    } else if (((EditText) findViewById(R.id.text_email)).getText().toString().length() < 10) {
                        ((EditText) findViewById(R.id.text_email)).setError("Enter Student Id of 9 digits");

                    } else {
                        userName = ((EditText) findViewById(R.id.text_email)).getText().toString();
                    }

                    if (((EditText) findViewById(R.id.userPass)).getText().toString().isEmpty() || ((EditText) findViewById(R.id.userPass)).getText().toString().equals("")) {

                        ((EditText) findViewById(R.id.userPass)).setError("Enter Password");
                    } else {

                        password = ((EditText) findViewById(R.id.userPass)).getText().toString();
                        for (StudentDetails studentDetails1 : studentDetailsList) {
                            if (studentDetails1.getStudentId().equalsIgnoreCase(userName) && studentDetails1.getPassword().equalsIgnoreCase(password)) {
                                isStudentExists = true;
                                studentDetails = studentDetails1;
                                break;
                            }
                        }
                        if (isStudentExists) {
                            Intent messageThreads = new Intent(MainActivity.this, VendorListActivity.class);
                            messageThreads.putExtra(STUDENT_OBJECT, studentDetails);
                            startActivity(messageThreads);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Incoreect UserName or Password!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }


            });

            findViewById(R.id.vendorClick).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, VendorActivity.class);
                    startActivity(intent);
                }
            });


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
}
