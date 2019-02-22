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

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    Button buttonHandlekurv;
    TextView textView;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        buttonHandlekurv = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

    }

    public void onButtonAdd(View view){
        /*ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearLayout);
        TextView tv = new TextView(this);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setText("smal sak");
        viewGroup.addView(tv);*/
        /*LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dishView = layoutInflater.inflate(R.layout.dish_layout, null);
        TextView textID = dishView.findViewById(R.id.textID);
        textID.setText("ID");
        TextView textName = dishView.findViewById(R.id.textName);
        textName.setText("Name");
        TextView textDesc = dishView.findViewById(R.id.textDesc);
        textDesc.setText("Description");
        TextView textPrice = dishView.findViewById(R.id.textPrice);
        textPrice.setText("Price");

        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.linearLayout);
        insertPoint.addView(dishView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));*/
    }

    /*private void addMenuToView(Menu menu){ //Parameter Menu-class
        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Dish dish : menu.getDishList()){
            View dishView = layoutInflater.inflate(R.layout.dish_layout, null);
            TextView textID = dishView.findViewById(R.id.textID);
            TextView textName = dishView.findViewById(R.id.textName);
            TextView textDesc = dishView.findViewById(R.id.textDesc);
            TextView textPrice = dishView.findViewById(R.id.textPrice);
            textID.setText(dish.getID());
            textName.setText(dish.getName());
            textDesc.setText(dish.getDesc());
            textPrice.setText(dish.getPrice());
        }
    }*/

    private String readStream(InputStream in){ //Idk but it works
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    private void sendRequest(String url) {
        // The requests are sent in cleartext over HTTP. Use HTTPS when sending passwords.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                textView.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText(error.toString());
            }
        });

        requestQueue.add(stringRequest);
    }
}
