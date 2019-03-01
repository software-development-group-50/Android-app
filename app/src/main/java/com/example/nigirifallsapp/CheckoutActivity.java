package com.example.nigirifallsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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


import java.util.List;
import java.util.HashMap;
import java.util.Map;


public class CheckoutActivity extends AppCompatActivity {

    public static final String OrderIntent = "CheckoutActivity.IntentString.Order";

    Button buttonPlaceOrder;
    Button buttonBackToMenu;
    RequestQueue requestQueue;
    //Order order;
    Map<Dish, Integer> numOfEachDish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_layout);
        this.buttonPlaceOrder = findViewById(R.id.button3);
        this.buttonBackToMenu = findViewById(R.id.returN);
        this.requestQueue = Volley.newRequestQueue(this);


        // Here the order and order-hashmap is fetched from the MenuActivity
        Intent intent = this.getIntent();
        //this.order = intent.getParcelableExtra(MenuActivity.OrderIntent);
        this.numOfEachDish = (HashMap<Dish, Integer>) intent.getSerializableExtra(MenuActivity.HashMapIntent);


        // Function to add every Dish-item from ArrayList order to the ScrollView
        for (Map.Entry<Dish, Integer> map : numOfEachDish.entrySet()) {
            if (map.getValue() != 0) {
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dishView = layoutInflater.inflate(R.layout.activity_dish_checkout, null);

                TextView textName = dishView.findViewById(R.id.textName);
                //TextView textDesc = dishView.findViewById(R.id.textDesc);
                TextView textQuantity = dishView.findViewById(R.id.textView3);
                TextView textPrice = dishView.findViewById(R.id.textPrice);
                TextView xTimes = dishView.findViewById(R.id.textView8);

                Dish dish = map.getKey();
                int price = dish.getPrice();
                textName.setText(dish.getName()); //textQuantity.setText(map.getValue().toString());
                textPrice.setText(map.getValue().toString(price)); // textPrice.setText(map.getValue().toString(price))
                textQuantity.setText(map.getValue().toString());


                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.linearLayout);
                insertPoint.addView(dishView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            }
        }
    }

    //go to remove or add items from checkout-menu
    public void onButtonBackToMenu(View view) {
        // Fix this shit
        Intent intent = new Intent(this, MenuActivity.class);
        //intent.putParcelableArrayListExtra(OrderIntent, (ArrayList<? extends Parcelable>) this.order);
        startActivity(intent);

    }

    private String orderHashMapToString() {
        StringBuilder sb = new StringBuilder();
        //sb.append(orderID).append("|")
        //        .append(customerID).append("|");
        //for (Dish dish : numOfEachDish.keySet()){
        for (Map.Entry<Dish, Integer> entry : this.numOfEachDish.entrySet()) {
            if (entry.getValue() != 0) {
                sb.append(entry.getKey().dishID).append("|")
                        .append(entry.getValue()).append("|");
            }
        }
        //sb.append(totalPrice).append(";");
        return sb.toString();
    }

    //transition to final state
    public void onButtonPlaceOrder(View view) {
        String url = "http://folk.ntnu.no/jennyjy/addorder.php/?order="; // Change URL
        url += orderHashMapToString();
        sendRequest(url);
        Intent intent = new Intent(this, ConfirmationActivity.class);
        //intent.putParcelableArrayListExtra(OrderIntent, (ArrayList<? extends Parcelable>) this.order);
        startActivity(intent);

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

    }

}
