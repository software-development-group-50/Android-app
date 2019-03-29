package com.example.nigirifallsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    SharedPreferences sharedPreferences;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        this.requestQueue = Volley.newRequestQueue(this);
        this.buttonConfirm = findViewById(R.id.buttonConfirm);
        this.buttonFinish = findViewById(R.id.buttonFinish);
        this.linearLayoutAdmin = findViewById(R.id.linearLayoutAdmin);
        this.sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        this.drawerLayout = findViewById(R.id.drawer_layout);
        setTitle("Orders for " + this.sharedPreferences.getString("locationString", "error"));
        this.location = this.sharedPreferences.getString("locationString", "error");
        setTitle("Orders for " + this.location);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
        this.navigationView = findViewById(R.id.nav_view);
        this.navigationView.getMenu().getItem(0).setChecked(true);
        this.navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();
                        onOptionsItemSelected(menuItem);
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        //sendRequestGetAllOrders("http://folk.ntnu.no/magnuti/getallorders.php");
        String url = "http://org.ntnu.no/nigiriapp/getallorders.php/?location=";
        url += this.location;

        this.sendRequestGetAllOrders(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.nav_admin_orders:
                return true;
            case R.id.nav_admin_reviews:
                Intent intent = new Intent(this, ReviewsActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_logout:
                logOutAlert();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void onActualResponseGetAllOrders(String response) {
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

    private void onActualResponseChangeStatus(String response) {
        // Reloads all the orders
        this.linearLayoutAdmin.removeAllViews();
        //sendRequestGetAllOrders("http://folk.ntnu.no/magnuti/getallorders.php");
        String url = "http://org.ntnu.no/nigiriapp/getallorders.php/?location=";
        url += this.location;

        this.sendRequestGetAllOrders(url);
    }

    // Function for adding all orders to the ScrollView and adding ClickListeners to them
    private void addMenuToView(List<OrderInAdmin> orderInAdminList) {
        // Reversed for-loop, since we want newest order on top
        for (int i = orderInAdminList.size() - 1; i >= 0; i--) {
            LayoutInflater outerLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View orderInAdminView = outerLayoutInflater.inflate(R.layout.order_in_admin_layout, null);
            final int orderid = Integer.valueOf(orderInAdminList.get(i).getOrderId().trim());

            final TextView textOrders = orderInAdminView.findViewById(R.id.textOrderID);
            final TextView textPickUpTime = orderInAdminView.findViewById(R.id.textPickUpTime);
            final TextView textOrderStatus = orderInAdminView.findViewById(R.id.textOrderStatus);

            textOrders.setText(orderInAdminList.get(i).getOrderId());
            String timeString = orderInAdminList.get(i).getPickUpTime();
            textPickUpTime.setText(timeString.substring(0, timeString.length() - 3));
            textOrderStatus.setText(orderInAdminList.get(i).getStatus());

            ViewGroup outerInsertPoint = (ViewGroup) findViewById(R.id.linearLayoutAdmin);
            outerInsertPoint.addView(orderInAdminView, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            //Index -1 -> older orders are placed to the bottom

            for (int k = 0; k < orderInAdminList.get(i).getDishList().size(); k += 2) {
                LayoutInflater innerLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dishInOrderInAdminView = innerLayoutInflater.inflate(R.layout.dish_in_order_in_admin_layout, null);

                TextView textDishName = dishInOrderInAdminView.findViewById(R.id.textDishName);
                TextView textQuantity = dishInOrderInAdminView.findViewById(R.id.textQuantity);

                textDishName.setText(orderInAdminList.get(i).getDishList().get(k));
                textQuantity.setText(orderInAdminList.get(i).getDishList().get(k + 1));

                ViewGroup innerInsertPoint = (ViewGroup) findViewById(R.id.linearLayoutOrder);
                innerInsertPoint.addView(dishInOrderInAdminView, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            }

            final LinearLayout linearLayoutOrder = findViewById(R.id.linearLayoutOrder);
            this.defaultColor = linearLayoutOrder.getSolidColor(); // I don´t know the default color in Android Studio, so it is fetched here.
            linearLayoutOrder.setId(orderInAdminList.size() - (i + 1)); // This line is required so that not all orders are placed into the same linearLayoutOrder.
            //The setID is indexed as if the IDs are counting from top to bottom (0, 1, 2...)
            final int linearLayoutOrderIndex = linearLayoutOrder.getId();

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

    public void onButtonWaiting(View view) {
        String url = "http://folk.ntnu.no/magnuti/changeorder.php/?status=Waiting&orderid=";
        url += Integer.toString(this.chosenDishID);
        this.sendRequestChangeStatus(url);
    }

    public void onButtonConfirm(View view) {
        String url = "http://folk.ntnu.no/magnuti/changeorder.php/?status=Confirmed&orderid=";
        url += Integer.toString(this.chosenDishID);
        this.sendRequestChangeStatus(url);
    }

    public void onButtonReady(View view){
        String url = "http://folk.ntnu.no/magnuti/changeorder.php/?status=Pick-upready&orderid=";
        url += Integer.toString(this.chosenDishID);
        this.sendRequestChangeStatus(url);
    }

    // This function is called on the hardware back-button on the phone.
    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            logOutAlert();
        }
    }

    private void logOutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logOut();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                navigationView.getMenu().getItem(0).setChecked(true);
                dialog.dismiss();
            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logOut() {
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        sp.edit().putBoolean("logged", false).apply();
        sp.edit().putString("phonenumber", null).apply();
        NavUtils.navigateUpFromSameTask(this); // This clears the Menu activity from the activity stack. Only Login activity now.
        /*Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // This clears all activities except Location and Login
        startActivity(intent);*/
    }
}
