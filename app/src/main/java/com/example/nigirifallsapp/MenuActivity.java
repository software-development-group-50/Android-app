package com.example.nigirifallsapp;

import android.content.Context;
import android.content.Intent;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import android.os.Parcel;
import android.os.Parcelable;


public class MenuActivity extends AppCompatActivity implements Parcelable{

    public static final String OrderIntent = "MenuActivity.IntentString.Order";
    Button buttonCheckout;
    RequestQueue requestQueue;
    String stringFromPHP;
    Menu menu;
    List<Dish> order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        requestQueue = Volley.newRequestQueue(this);
        sendRequest("http://folk.ntnu.no/magnuti/getalldish.php");
        buttonCheckout = findViewById(R.id.buttonCheckout);
        this.order = new ArrayList<>();
    }

    private void onActualResponse(String response){
        stringFromPHP = response;
        this.menu = new Menu();
        menu.updateMenu(stringFromPHP);
        addMenuToView(this.menu);
    }

    public void onButtonCheckout(View view){
        Intent intent = new Intent(this, ThirdActivity.class);
        intent.putParcelableArrayListExtra(OrderIntent, (ArrayList<? extends Parcelable>) this.order);
        startActivity(intent);
    }

    private void onAddDish(Dish dish){
        this.order.add(dish);
        //buttonCheckout.setText(dish.getName());
    }

    private void onRemoveDish(Dish dish){
        try{
            this.order.remove(dish);
        } catch (Exception e){

        }
    }

    private void addMenuToView(Menu menu){ //Parameter Menu-class
        final List<Dish> tempDishList = menu.getDishList();
        for(int i = 0; i < tempDishList.size(); i++){
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dishView = layoutInflater.inflate(R.layout.dish_layout, null);

            TextView textName = dishView.findViewById(R.id.textName);
            TextView textDesc = dishView.findViewById(R.id.textDesc);
            TextView textPrice = dishView.findViewById(R.id.textPrice);
            final Button buttonPluss = dishView.findViewById(R.id.buttonPluss);
            final Button buttonMinus = dishView.findViewById(R.id.buttonMinus);

            textName.setText(tempDishList.get(i).getName());
            textDesc.setText(tempDishList.get(i).getDesc());
            textPrice.setText(Integer.toString(tempDishList.get(i).getPrice()));
            buttonPluss.setId(i);
            buttonMinus.setId(i);
            buttonPluss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddDish(tempDishList.get(buttonPluss.getId()));
                }
            });
            buttonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRemoveDish(tempDishList.get(buttonMinus.getId()));
                }
            });

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
                onActualResponse(response); //Lol remove this
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
