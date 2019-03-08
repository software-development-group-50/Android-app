package com.example.nigirifallsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    Button registerBtn;
    EditText phoneNr;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNr = (EditText)findViewById(R.id.input_nr);
        password = (EditText)findViewById(R.id.input_password);
        this.loginBtn = findViewById(R.id.login_login);
        this.registerBtn = findViewById(R.id.Register1);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(phoneNr.getText().toString(), password.getText().toString());
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /*
    //Function for moving to the Menu-activity
    public void onButtonMenu(View view){


    }


    //move to registerActivity
    public void onButtonRegister(View view){



    }*/


    //hvis den gitte kombinasjonen er riktig så hopper vi til neste activity(menu)
    //ønsker senere at hvis denne er lik en verdi fra databasen, da blir den gyldig/beveger oss til neste aktivitet
    public void validate(String number, String password){
        if((number.length() == 8) && (password.equals("sushi"))){
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);

        }

    }













}
