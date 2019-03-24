package com.example.nigirifallsapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn;
    EditText name;
    EditText phoneNr;
    EditText password;
    EditText confirm;
    TextView error_confirm;
    TextView error_nr;
    RequestQueue requestQueue;


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
        this.requestQueue = Volley.newRequestQueue(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // This line adds the back-button on the ActionBar


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
            String url = "https://org.ntnu.no/nigiriapp/register.php/?userID=";
            url += number;
            url += "&password=";
            url += password;
            url += "&name=";
            url += name;
            sendRequest(url);
        } else {
            if (!confirm.equals(password)) {
                error_confirm.setText("Error 203: Confirm password does not match password");
            }

            if (number.length() != 8) {
                error_nr.setText("Invalid number. A number must consist of 8 digits.");
            }
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

    private void onActualResponse(String response){
        if(response.trim().equals("1")){
            Intent menuIntent = new Intent(this, MenuActivity.class);
            startActivity(menuIntent);
        } else {
            error_nr.setText(response);
        }
    }
}
