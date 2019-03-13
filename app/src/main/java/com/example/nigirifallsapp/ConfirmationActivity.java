package com.example.nigirifallsapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Color;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class ConfirmationActivity extends AppCompatActivity {
    private String orderID = "Sorry something went wrong";
    private String statusText = "Order Status: ";
    private TextView statusTextView;
    private String orderStatus = "Error";
    private String pickUpTime = "68:23:20";
    RequestQueue requestQueue;
    Color NYELLOW = new Color();

    Thread thread = new Thread() {
        @Override
        public void run() {
            int count = 0;
            while(count < 100) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestOrderInfo();
                    }
                });
                count += 1;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initOrderID();
        setContentView(R.layout.confirmation_activity_layout);
        this.requestQueue = Volley.newRequestQueue(this);

        TextView textView2 = findViewById(R.id.textView2);
        String text = "Your order has been registered! Ready for pickup in about 30 min. \n\n\nYour pickup reference is: \n ";
        text += getOrderID() +"\n\n\n\n";
        textView2.setTextColor(getResources().getColor(R.color.textColorDark));
        textView2.setText(text);
        textView2.setTextSize(18);

        //Order status
        this.statusTextView = findViewById(R.id.textView4);
        String status = statusText + "Waiting";
        statusTextView.setText(status);
        statusTextView.setTextColor(getResources().getColor(R.color.waitingColor));
        statusTextView.setTextSize(24);
        thread.start();
    }

    private void initOrderID() {
        String newOrderID = getIntent().getStringExtra("OrderID");

        if (newOrderID != null) {
            this.orderID = newOrderID.replaceAll("\\s","");
            System.out.print(this.orderID);
        }
    }

    private String getOrderID() {
        return this.orderID;
    }
    private String getOrderStatus() {
        return this.orderStatus;
    }

    private String getPickUpTime() {
        return this.pickUpTime;
    }

    private void requestOrderInfo() {
        String url = "http://folk.ntnu.no/jennyjy/getorder.php/?OrderID="; // Change URL
        url += getOrderID();
        sendRequest(url);
    }

    // Function for sending a HTTP request to the PHP-script
    private void sendRequest(String url) {
        // The requests are sent in cleartext over HTTP. Use HTTPS when sending passwords.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onActualResponse(response); // The extra function is needed because of the scope of the @Override function
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);
    }

    private void onActualResponse(String response) {
        String[] orderInfo = response.split("\\|");
        this.pickUpTime = orderInfo[1];
        this.orderStatus = orderInfo[2];
        updateOrderInfo();
    }

    private void updateOrderInfo() {
        String status = statusText + orderStatus;
        statusTextView.setText(status);

        switch (orderStatus) {
            case "Waiting":
                statusTextView.setTextColor(getResources().getColor(R.color.waitingColor));
                break;
            case "Confirmed":
                statusTextView.setTextColor(getResources().getColor(R.color.confirmedColor));
                break;
            case "Pickup-ready":
                statusTextView.setTextColor(getResources().getColor(R.color.pickupColor));
                break;
            case "Canceled":
                statusTextView.setTextColor(getResources().getColor(R.color.canceledColor));
                break;
        }
    }
}
