package com.example.nigirifallsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {


    TextView showValue;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        showValue = (TextView) findViewById(R.id.countervalue);
    }


    //ved hvert klikk på denne knappen vil verdien til countervalue øke
    public void countIN(View view){
        counter++;
        showValue.setText(Integer.toString(counter));
    }

    //ved hvert klikk på denne knappen vil verdien til countervalue minke
    public void countDown(View v){
        counter--;
        showValue.setText(Integer.toString(counter));
    }
}
