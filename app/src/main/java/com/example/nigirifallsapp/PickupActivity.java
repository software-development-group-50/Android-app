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

public class PickupActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Button placeOrderBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

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
        textView.setText("Time:     " + hourOfDay + ":" + min);

    }

    public void onButtonPlaceOrder(View view){
        Intent intent = new Intent(this, ConfirmationActivity.class);
        startActivity(intent);

    }

}




































































