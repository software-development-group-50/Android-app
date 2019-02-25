package com.example.nigirifallsapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    Button buttonCheckout;
    RequestQueue requestQueue;
    String stringFromPHP;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        sendRequest("http://folk.ntnu.no/magnuti/getalldish.php");
        buttonCheckout = findViewById(R.id.buttonCheckout);
    }

    private void onActualResponse(String response){
        stringFromPHP = response;
        this.menu = new Menu();
        menu.updateMenu(stringFromPHP);
        addMenuToView(this.menu);
    }

    private void onErrorResponse(VolleyError error){

    }

    public void onButtonCheckout(View view){
        // Go to checkout activity
    }

    private void addMenuToView(Menu menu){ //Parameter Menu-class
        for (Dish dish : menu.getDishList()){
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dishView = layoutInflater.inflate(R.layout.dish_layout, null);
            TextView textID = dishView.findViewById(R.id.textID);
            TextView textName = dishView.findViewById(R.id.textName);
            TextView textDesc = dishView.findViewById(R.id.textDesc);
            TextView textPrice = dishView.findViewById(R.id.textPrice);
            textID.setText(Integer.toString(dish.getID()));
            textName.setText(dish.getName());
            textDesc.setText(dish.getDesc());
            textPrice.setText(Integer.toString(dish.getPrice()));

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.linearLayout);
            insertPoint.addView(dishView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        }
    }

    private String readStream(InputStream in){ //Idk but it works
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    private void sendRequest(String url) {
        // The requests are sent in cleartext over HTTP. Use HTTPS when sending passwords.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onActualResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onErrorResponse(error);
            }
        });

        requestQueue.add(stringRequest);
    }
}
