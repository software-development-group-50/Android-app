package com.example.nigirifallsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    Button buttonToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.buttonToMenu = findViewById(R.id.button6);
    }



    public void onButtonToMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        //intent.putParcelableArrayListExtra(OrderIntent, (ArrayList<? extends Parcelable>) this.order);
        startActivity(intent);
    }



}
