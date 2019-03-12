package com.example.nigirifallsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PickupActivity extends AppCompatActivity {

    Button returnBtn;
    Button placeOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        this.returnBtn = findViewById(R.id.return_);
        this.placeOrderBtn = findViewById(R.id.place_order);
    }


    public void onButtonPlaceOrder(View view){
        Intent intent = new Intent(this, ConfirmationActivity.class);
        startActivity(intent);

    }

    public void onButtonReturn(View view){
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }
}




































































