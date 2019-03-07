package com.example.nigirifallsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ConfirmationActivity extends AppCompatActivity {
    private String orderID = "nothing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.updateOrderID();
        setContentView(R.layout.confirmation_activity_layout);

        TextView textView2 = findViewById(R.id.textView2);
        String text = "Your order has been registered! Ready for pickup in about 30 min. \n\nPickup reference: ";
        text += getOrderID();
        textView2.setText(text); //append(this.orderID);
    }

    private void updateOrderID(){
        String newOrderID = getIntent().getStringExtra("OrderID");

        if (newOrderID != null) {
            this.orderID = newOrderID;
        }
    }
    private String getOrderID(){
        return this.orderID;
    }
}
