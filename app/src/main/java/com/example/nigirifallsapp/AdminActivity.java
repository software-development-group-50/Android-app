package com.example.nigirifallsapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    Button buttonConfirm;
    Button buttonFinish;
    LinearLayout linearLayoutAdmin;
    private int chosenDishIndex;
    private int chosenDishID;
    private int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        this.requestQueue = Volley.newRequestQueue(this);
        this.buttonConfirm = findViewById(R.id.buttonConfirm);
        this.buttonFinish = findViewById(R.id.buttonFinish);
        this.linearLayoutAdmin = findViewById(R.id.linearLayoutAdmin);
        this.chosenDishIndex = 0; //To prevent null-pointer exception

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // This line adds the back-button on the ActionBar

        this.sendRequestGetAllOrders("http://folk.ntnu.no/magnuti/getallorders.php");
    }

    // Function for sending a HTTP request to the PHP-script
    private void sendRequestGetAllOrders(String url) {
        // The requests are sent in cleartext over HTTP. Use HTTPS when sending passwords.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onActualResponseGetAllOrders(response); // The extra function is needed because of the scope of the function
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);
    }

    private void onActualResponseGetAllOrders(String response){
        List<OrderInAdmin> orderList = new ArrayList<>();
        String[] arrayWithStringOrders = response.split(";");
        for (String elementsInStringArray : arrayWithStringOrders) {
            OrderInAdmin orderInAdmin = new OrderInAdmin(elementsInStringArray);
            orderList.add(orderInAdmin);
        }
        addMenuToView(orderList);
    }

    // Function for sending a HTTP request to the PHP-script
    private void sendRequestChangeStatus(String url) {
        // The requests are sent in cleartext over HTTP. Use HTTPS when sending passwords.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onActualResponseChangeStatus(response); // The extra function is needed because of the scope of the function
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);
    }

    private void onActualResponseChangeStatus(String response){
        // Reloads all the orders, but not the text Orders.
        this.linearLayoutAdmin.removeViews(1, linearLayoutAdmin.getChildCount() - 1);
        sendRequestGetAllOrders("http://folk.ntnu.no/magnuti/getallorders.php");
    }

    // Function for adding all orders to the ScrollView and adding ClickListeners to them
    private void addMenuToView(List<OrderInAdmin> orderInAdminList){
        // Starts at 1 so the text Orders is not affected.
        for (int i = 0; i < orderInAdminList.size(); i++){
            LayoutInflater outerLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View orderInAdminView = outerLayoutInflater.inflate(R.layout.order_in_admin_layout, null);
            final int orderid = Integer.valueOf(orderInAdminList.get(i).getOrderId().trim());

            final TextView textOrders = orderInAdminView.findViewById(R.id.textOrderID);
            final TextView textPickUpTime = orderInAdminView.findViewById(R.id.textPickUpTime);
            final TextView textOrderStatus = orderInAdminView.findViewById(R.id.textOrderStatus);

            String timeString = orderInAdminList.get(i).getPickUpTime();
            textOrders.setText(orderInAdminList.get(i).getOrderId());
            textPickUpTime.setText(timeString.substring(0, timeString.length()-3));
            textOrderStatus.setText(orderInAdminList.get(i).getStatus());

            ViewGroup outerInsertPoint = (ViewGroup) findViewById(R.id.linearLayoutAdmin);
            outerInsertPoint.addView(orderInAdminView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            // The 0 index specifies that the element is added FIRST to the ScrollView

            for (int k = 0; k < orderInAdminList.get(i).getDishList().size(); k += 2){
                LayoutInflater innerLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dishInOrderInAdminView = innerLayoutInflater.inflate(R.layout.dish_in_order_in_admin_layout, null);

                TextView textDishName = dishInOrderInAdminView.findViewById(R.id.textDishName);
                TextView textQuantity = dishInOrderInAdminView.findViewById(R.id.textQuantity);

                textDishName.setText(orderInAdminList.get(i).getDishList().get(k));
                textQuantity.setText(orderInAdminList.get(i).getDishList().get(k + 1));

                ViewGroup innerInsertPoint = (ViewGroup) findViewById(R.id.linearLayoutOrder);
                innerInsertPoint.addView(dishInOrderInAdminView, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                // The -1 index specifies that the element is added LAST to the ScrollView
            }

            final LinearLayout linearLayoutOrder = findViewById(R.id.linearLayoutOrder);
            this.defaultColor = linearLayoutOrder.getSolidColor(); // I don´t know the default color in Android Studio, so it is fetched here.
            // The +1 is needed because the text Orders has index 0, therefore, the getChildAt is indexed +1.
            linearLayoutOrder.setId(i); // This line is required so that not all orders are placed into the same linearLayoutOrder.
            final int linearLayoutOrderIndex = linearLayoutOrder.getId(); //Aka i

            this.linearLayoutAdmin.getChildAt(linearLayoutOrderIndex).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayoutAdmin.getChildAt(chosenDishIndex).setBackgroundColor(defaultColor);
                    linearLayoutAdmin.getChildAt(linearLayoutOrderIndex).setBackgroundColor(Color.GRAY);
                    chosenDishIndex = linearLayoutOrderIndex;
                    chosenDishID = orderid;
                }
            });
        }
    }

    public void onButtonNew(View view){
        String url = "http://folk.ntnu.no/magnuti/changeorder.php/?status=Waiting&orderid=";
        url += Integer.toString(this.chosenDishID);
        this.sendRequestChangeStatus(url);
    }

    public void onButtonConfirm(View view){
        String url = "http://folk.ntnu.no/magnuti/changeorder.php/?status=Confirm&orderid=";
        url += Integer.toString(this.chosenDishID);
        this.sendRequestChangeStatus(url);
    }

    public void onButtonFinish(View view){
        String url = "http://folk.ntnu.no/magnuti/changeorder.php/?status=Ready&orderid=";
        url += Integer.toString(this.chosenDishID);
        this.sendRequestChangeStatus(url);
    }
}
