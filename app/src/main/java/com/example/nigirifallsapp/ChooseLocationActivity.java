package com.example.nigirifallsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ChooseLocationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_location_activity_layout);
        this.sharedPreferences = getSharedPreferences("location", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void onButtonOslo(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.sharedPreferences.edit().putString("locationString", "Oslo");
    }

    public void onButtonTrondheim(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.sharedPreferences.edit().putString("locationString", "Trondheim");
    }

    public void onButtonBergen(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.sharedPreferences.edit().putString("locationString", "Bergen");
    }


}
