package com.example.nigirifallsapp;

import android.content.Intent;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class PickupActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Button placeOrderBtn;
    int hourOfDay;
    int min;
    public static final String HourIntent = "PickUpActivity.IntentString.Hour";
    public static final String MinIntent = "PickUpActivity.IntentString.Min";
    private String url;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);
        Intent intent = this.getIntent();
        this.url = intent.getStringExtra(CheckoutActivity.OrderIntent);
        this.requestQueue = Volley.newRequestQueue(this);

        this.placeOrderBtn = findViewById(R.id.place_order);
        Button chooseTime = (Button) findViewById(R.id.button);
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Pick time");
            }
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int min){
        this.hourOfDay = hourOfDay;
        this.min = min;
        TextView textView = (TextView) findViewById(R.id.textView10);
        textView.setText("Time:     " + hourOfDay + ":" + min);

    }

    public void onButtonPlaceOrder(View view){
        this.url += "&time=";
        this.url += Integer.toString(this.hourOfDay) + ":" + Integer.toString(this.min) + ":00";
        sendRequest(this.url);
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

        this.requestQueue.add(stringRequest);
    }

    private void onActualResponse(String response) {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(HourIntent, this.hourOfDay);
        bundle.putInt(MinIntent, this.min);
        intent.putExtras(bundle);

        startActivity(intent);
    }

}





























































