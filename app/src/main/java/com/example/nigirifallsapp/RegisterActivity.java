package com.example.nigirifallsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn;
    EditText name;
    EditText phoneNr;
    EditText password;
    EditText confirm;
    TextView error_confirm;
    TextView error_nr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = (Button) findViewById(R.id.Register);
        name = (EditText) findViewById(R.id.editText2);
        phoneNr = (EditText) findViewById(R.id.editText6);
        password = (EditText) findViewById(R.id.editText3);
        confirm = (EditText) findViewById(R.id.editText4);
        error_confirm = (TextView) findViewById(R.id.error_confirm);
        error_nr = (TextView) findViewById(R.id.error_nr);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(name.getText().toString(), phoneNr.getText().toString(), password.getText().toString(), confirm.getText().toString());
            }
        });
    }

    //hvis navn, nummer, passord og bekreft passord er fylt ut, så vil registrer knappen få funksjonalitet og vi kan gå videre
    public void validate(String name, String number, String password, String confirm) {

        error_confirm.setText("");
        error_nr.setText("");
        if ((name.length() > 0) && (number.length() == 8) && (password.length() > 0) && (confirm.equals(password))) {
            Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
            startActivity(intent);
        } else {
            if (!confirm.equals(password)) {
                error_confirm.setText("Error! Confirm password does not match password");

            }

            if (number.length() != 8) {
                error_nr.setText("Invalid number! A number must consist of 8 digits");

            }
        }

    }
}
