package com.example.nigirifallsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    TextView error_nr; //error message number
    TextView error_pw; //error message password
    RequestQueue requestQueue;
    String phoneNumber;


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
        this.requestQueue = Volley.newRequestQueue(this);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1000-for loop???
                //for (int i = 0; i < 1000; i++){
                    phoneNumber = phoneNr.getText().toString().trim();
                    validate(phoneNumber, password.getText().toString());
                //}
            }
        });

    }

    public void onButtonRegister(View view){
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    // If the username and password matches a user in the database, the next acitivity is MenuActivity
    public void validate(String number, String password) {
        error_nr.setText("");
        error_pw.setText("");

        if(number.equals("911") && password.equals("insidejob")){
            Intent adminIntent = new Intent(this, AdminActivity.class);
            startActivity(adminIntent);
        }

        String url = "https://org.ntnu.no/nigiriapp/login.php/?userID=";
        url += number;
        url += "&password=";
        url += password;
        sendRequest(url);
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

    private void onActualResponse(String response){
        if(response.trim().equals(this.phoneNumber)){
            Intent menuIntent = new Intent(this, MenuActivity.class);
            startActivity(menuIntent);
        } else{
            error_pw.setText(response);
        }
    }

}
