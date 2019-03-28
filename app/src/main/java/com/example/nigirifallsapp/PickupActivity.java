package com.example.nigirifallsapp;

import android.content.Intent;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
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
    Boolean bool = true;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);
        Intent intent = this.getIntent();
        this.url = intent.getStringExtra(CheckoutActivity.OrderIntent);
        this.requestQueue = Volley.newRequestQueue(this);
        this.sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        this.errorText = findViewById(R.id.error_message_pickup);
        this.placeOrderBtn = findViewById(R.id.place_order);
        this.textTime = findViewById(R.id.textTime);
        Button chooseTime = (Button) findViewById(R.id.button);

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
    public void onTimeSet(TimePicker view, int hourOfDay, int min){
        this.hourOfDay = hourOfDay;
        this.min = min;
        double time_selected = hourOfDay + min/60;
        Calendar c = Calendar.getInstance();
        int hour_ = c.get(Calendar.HOUR_OF_DAY);
        int min_ = c.get(Calendar.MINUTE);
        double time_now = hour_ + min_/60;

        for (int i = 0;i <1000;i++) {
            this.textTime.setText("");
            this.errorText.setText("");
            if (time_selected >= (time_now + 0.5 )) {
                this.bool = true;
                this.textTime.setText("Time:      " + hourOfDay + ":" + min);
            } else {
                this.bool = false;
                this.errorText.setText("Invalid time! Cannot choose a pick-up time earlier than in 30 minutes");
            }
        }
    }

    public void onButtonPlaceOrder(View view){
        this.placeOrderBtn.setEnabled(false);
        this.url += "&time=";
        this.url += Integer.toString(this.hourOfDay) + ":" + Integer.toString(this.min) + ":00";
        this.url += "&location=";
        this.url += sharedPreferences.getString("locationString","error");
        this.url += "&userID=";
        this.url += sharedPreferences.getString("phonenumber", "error");
        Log.e("Url", url);
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