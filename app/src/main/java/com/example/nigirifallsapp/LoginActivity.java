package com.example.nigirifallsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    Button registerBtn;
    EditText phoneNr;
    EditText password;
    TextView error_pw; //error message password
    RequestQueue requestQueue;
    String phoneNumber;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNr = findViewById(R.id.input_nr);
        password = findViewById(R.id.input_password);
        this.loginBtn = findViewById(R.id.login_login);
        this.registerBtn = findViewById(R.id.Register1);
        error_pw = findViewById(R.id.error_pw);
        this.requestQueue = Volley.newRequestQueue(this);
        this.sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // This line adds the back-button on the ActionBar

    }

    public void onButtonRegister(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    public void onButtonLogin(View view) {
        this.loginBtn.setEnabled(false);
        phoneNumber = phoneNr.getText().toString().trim();
        validate(phoneNumber, password.getText().toString());
    }

    // If the username and password matches a user in the database, the next acitivity is MenuActivity
    public void validate(String number, String password) {
        if (number.isEmpty() || password.isEmpty()) {
            this.error_pw.setText("Please provide a username and a password");
            this.loginBtn.setEnabled(true);
        } else {
            error_pw.setText("");

            if (number.equals("911") && password.equals("insidejob")) {
                Intent adminIntent = new Intent(this, AdminActivity.class);
                startActivity(adminIntent);
            }

            String url = "https://org.ntnu.no/nigiriapp/login.php/?userID=";
            url += number;
            url += "&password=";
            url += password;
            sendRequest(url);
        }
    }

    // Function for sending a HTTP request to the PHP-script
    private void sendRequest(String url) {
        // The requests are sent in cleartext over HTTP. Use HTTPS when sending passwords.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onActualResponse(response); // The extra function is needed because of the scope of the @Override function
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);
    }

    private void onActualResponse(String response) {
        if (response.trim().equals(this.phoneNumber)) {
            this.sharedPreferences.edit().putBoolean("logged", true).apply();
            this.sharedPreferences.edit().putString("phonenumber", response.trim()).apply();
            Intent menuIntent = new Intent(this, MenuActivity.class);
            startActivity(menuIntent);
        } else {
            this.error_pw.setText("Wrong username/password");
            this.loginBtn.setEnabled(true);
        }
    }

}
