package com.msaini.cafetriamanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

public class VendorListActivity extends AppCompatActivity {

    StudentDetails studentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vendor_list);

        if (isConnected()) {
            if (getIntent() != null) {
                studentDetails = (StudentDetails) getIntent().getSerializableExtra(MainActivity.STUDENT_OBJECT);


                ((TextView) findViewById(R.id.welcomeText)).setText("Welcome " + studentDetails.getName());

                Gson gson = new Gson();
                String userString = gson.toJson(studentDetails);

                SharedPreferences sharedPref = this.getSharedPreferences(
                        "myprefs", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
               // editor.putString("token", user.token);
                editor.putString("user", userString);
                editor.commit();

                findViewById(R.id.bojang).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(VendorListActivity.this, FoodListActivity.class);
                        intent.putExtra("VENDOR_LIST", "1");
                        startActivity(intent);

                    }
                });

                findViewById(R.id.myOrder).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(VendorListActivity.this, ViewMyOrderActivity.class);
                        startActivity(intent);
                    }
                });

            }
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
