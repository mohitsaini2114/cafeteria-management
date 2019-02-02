package com.msaini.cafetriamanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class FinalTransaction extends AppCompatActivity {

    double ninerWallet = 0.0;
    double finalAmount = 0.0;
    String remainingAmount;
    StudentDetails user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_transaction);
      //  ((TextView)findViewById(R.id.amountError)).setVisibility(View.INVISIBLE);

        findViewById(R.id.logoutButtonVendor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalTransaction.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        if(getIntent() != null) {

           /* Bundle gt=getIntent().getExtras();
            String amount =gt.getString("final_amount");*/

            //Double amount =  getIntent().getExtras().getDouble("final_amount");

            FinalOrder finalOrder = (FinalOrder) getIntent().getSerializableExtra("final_amount");
            if (finalOrder != null) {
                finalAmount = Double.parseDouble(finalOrder.totalAmount);

                SharedPreferences sharedPref = this.getSharedPreferences(
                        "myprefs", Context.MODE_PRIVATE);

                String userString = sharedPref.getString("user", null);

                Gson gson = new Gson();

                user = gson.fromJson(userString, StudentDetails.class);
                ninerWallet = Double.parseDouble(user.getNinerWallet());

                ((TextView) findViewById(R.id.token)).setText(finalOrder.orderNumber + "");



                    remainingAmount = String.valueOf(ninerWallet - finalAmount);

                    DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("user").child(user.studentKey);
                    Map<String, Object> updateObject = new HashMap<>();
                    updateObject.put("ninerWallet", remainingAmount);
                    r.updateChildren(updateObject);

                    ((TextView) findViewById(R.id.availableBalance)).setText(finalAmount + " has been deducted from your Niner Wallet.");
            }
            else{
                TextView message = ((TextView) findViewById(R.id.message));
                message.setText("Your Niner wallet has insufficient funds to process this transaction. Please reload Niner wallet and try again.");
                message.setTextColor(Color.RED);
                ((TextView) findViewById(R.id.tokenText)).setVisibility(View.INVISIBLE);
                ((TextView) findViewById(R.id.token)).setVisibility(View.INVISIBLE);
            }
        }
    }
}
