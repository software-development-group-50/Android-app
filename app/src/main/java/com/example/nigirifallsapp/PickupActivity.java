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

import java.util.Calendar;

public class PickupActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Button placeOrderBtn;
    TextView errorText;
    Boolean bool = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        this.errorText = findViewById(R.id.error_message_pickup);
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
        TextView textView = (TextView) findViewById(R.id.textView10);
        Calendar c = Calendar.getInstance();
        int hour_ = c.get(Calendar.HOUR_OF_DAY);
        int min_ = c.get(Calendar.MINUTE);
        for (int i = 0;i <1000;i++) {
            textView.setText("");
            errorText.setText("");
            if (hourOfDay >= hour_ && min >= min_) {
                this.bool = true;
                textView.setText("Time:      " + hourOfDay + ":" + min);
            } else {
                this.bool = false;
                errorText.setText("Invalid time! Cannot choose a time earlier than the current time");

            }
        }
    }

    public void onButtonPlaceOrder(View view){
        if(bool) {
            Intent intent = new Intent(this, ConfirmationActivity.class);
            startActivity(intent);
        }

    }

}