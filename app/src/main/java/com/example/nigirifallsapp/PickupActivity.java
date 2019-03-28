package com.example.nigirifallsapp;

import android.content.Intent;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

import java.util.Calendar;

public class PickupActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Button placeOrderBtn;
    int hourOfDay;
    int min;
    public static final String HourIntent = "PickUpActivity.IntentString.Hour";
    public static final String MinIntent = "PickUpActivity.IntentString.Min";
    public static final String OrderIDIntent = "PickUpActivity.IntentString.OrderID";
    private String url;
    RequestQueue requestQueue;
    TextView errorText;
    TextView textTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);
        Intent intent = this.getIntent();
        this.url = intent.getStringExtra(CheckoutActivity.OrderIntent);
        this.requestQueue = Volley.newRequestQueue(this);

        this.errorText = findViewById(R.id.error_message_pickup);
        this.placeOrderBtn = findViewById(R.id.place_order);
        this.textTime = findViewById(R.id.textTime);
        Button chooseTime = findViewById(R.id.button);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // This line adds the back-button on the ActionBar

        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Pick time");
            }
        });
    }

    // This functions overrides the bar back-button so that it has the same function as the hardware back-button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int min) {
        //Cancer code
        this.textTime.setText("");
        this.errorText.setText("");

        this.hourOfDay = hour;
        this.min = min;
        double time_selected = (double) (hour) + ((double) min / 60);
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMin = calendar.get(Calendar.MINUTE);
        double time_now = (double) currentHour + ((double) currentMin / 60);

        if (hour < 8) {
            this.placeOrderBtn.setEnabled(false);
            this.errorText.setText("We do not deliver before 08:00");
        } else if (hour >= 22) {
            this.placeOrderBtn.setEnabled(false);
            this.errorText.setText("We do not deliver after 22:00");
        } else if (time_selected < (time_now + 0.5)) {
            this.placeOrderBtn.setEnabled(false);
            int orderHour = currentHour;
            int orderMin = currentMin + 30;
            if(orderMin > 59){
                orderHour++;
                orderMin -= 60;
            }
            if (orderMin < 10) {
                this.errorText.setText("Cannot choose a pick-up time earlier than " + Integer.toString(orderHour) + ":0" + Integer.toString(orderMin));
            } else {
                this.errorText.setText("Cannot choose a pick-up time earlier than " + Integer.toString(orderHour) + ":" + Integer.toString(orderMin));
            }
        } else {
            this.placeOrderBtn.setEnabled(true);
            this.textTime.setText("Time:      " + hour + ":" + min);
        }

    }

    public void onButtonPlaceOrder(View view) {
        this.url += "&time=";
        this.url += Integer.toString(this.hourOfDay) + ":" + Integer.toString(this.min) + ":00";
        //Log.e("Url", url);
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
        bundle.putString(OrderIDIntent, response);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}