package com.example.nigirifallsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Button button = (Button) findViewById(R.id.button);

        goToSecondActivity();

    }


    private void goToSecondActivity() {
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));

                //goToSecondActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }
/*
    public void goToThirdActivity(View view){
        Intent intent = new Intent(this, SecondActivity.class);

        startActivity(intent);
    }
*/



}
