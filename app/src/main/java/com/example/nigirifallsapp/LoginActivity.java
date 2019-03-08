package com.example.nigirifallsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    Button registerBtn;
    EditText phoneNr;
    EditText password;
    TextView error_nr; //error message number
    TextView error_pw; //error message password


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNr = (EditText)findViewById(R.id.input_nr);
        password = (EditText)findViewById(R.id.input_password);
        this.loginBtn = findViewById(R.id.login_login);
        this.registerBtn = findViewById(R.id.Register1);
        error_nr = (TextView)findViewById(R.id.error_nr);
        error_pw = (TextView)findViewById(R.id.error_pw);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 1000; i++){
                    validate(phoneNr.getText().toString(), password.getText().toString());
                }
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
    public void validate(String number, String password) {
        error_nr.setText("");
        error_pw.setText("");

        if ((number.length() == 8) && (password.equals("sushi"))) {
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(intent);

        }
        //error messages if one or both of the input are not valid
        else {
            if (number.length() != 8) {
                error_nr.setText("Invalid number! A number must consist of 8 digits");

            }

            if (!password.equals("sushi")){
                error_pw.setText("Invalid password");

            }

        }
    }


    /*public void cleanUpErrorMessage(){
        if(){

        }}*/









}
