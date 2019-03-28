package com.example.nigirifallsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ChooseLocationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_location_activity_layout);

        this.sharedPreferences = getSharedPreferences("login", MODE_PRIVATE); // Rename from login

        //Checking if user is already logged in
        //With this method the Login Acticity is never opened, so when the user logs out, this activity will open, not Login.
        if (this.sharedPreferences.getBoolean("logged", false)) {
            Intent menuIntent = new Intent(this, MenuActivity.class);
            startActivity(menuIntent);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void onButtonOslo(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.sharedPreferences.edit().putString("locationString", "Oslo").apply();
    }

    public void onButtonTrondheim(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.sharedPreferences.edit().putString("locationString", "Trondheim").apply();
    }

    public void onButtonBergen(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.sharedPreferences.edit().putString("locationString", "Bergen").apply();
    }


}
