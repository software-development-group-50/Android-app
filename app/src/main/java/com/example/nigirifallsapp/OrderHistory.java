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

public class OrderHistory extends AppCompatActivity {

    RequestQueue requestQueue;
    LinearLayout linearLayoutHistory;
    private int chosenDishIndex;
    //private int chosenDishID;
    private int defaultColor;
    SharedPreferences sharedPreferences;
    String userID;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        this.requestQueue = Volley.newRequestQueue(this);
        this.linearLayoutHistory = findViewById(R.id.linearLayoutHistory);
        this.drawerLayout = findViewById(R.id.drawer_layout);
        this.sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        this.userID = sharedPreferences.getString("phonenumber", "error");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
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
        this.sendRequestGetUserOrders("http://org.ntnu.no/nigiriapp/getuserorders.php/?userID=" + userID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.nav_menu:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.nav_logout:
                logOutAlert();
                return true;
            case R.id.nav_myOrders:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendRequestGetUserOrders(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onActualResponseGetUserOrders(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });

        requestQueue.add(stringRequest);
    }

    private void onActualResponseGetUserOrders(String response){
        List<OrderInAdmin> orderList = new ArrayList<>();
        String[] arrayWithStringOrders = response.split(";");
        for (String elementsInStringArray : arrayWithStringOrders) {
            OrderInAdmin orderInAdmin = new OrderInAdmin(elementsInStringArray);
            orderList.add(orderInAdmin);
        }
        addMenuToView(orderList);
    }

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

            ViewGroup outerInsertPoint = (ViewGroup) findViewById(R.id.linearLayoutHistory);
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

            this.linearLayoutHistory.getChildAt(linearLayoutOrderIndex).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayoutHistory.getChildAt(chosenDishIndex).setBackgroundColor(defaultColor);
                    linearLayoutHistory.getChildAt(linearLayoutOrderIndex).setBackgroundColor(Color.GRAY);
                    chosenDishIndex = linearLayoutOrderIndex;
                    //chosenDishID = orderid;
                }
            });
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
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void logOut() {
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        sp.edit().putBoolean("logged", false).apply();
        sp.edit().putString("phonenumber", null).apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // This clears all activities except Location and Login
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            NavUtils.navigateUpFromSameTask(this);
            /*Intent intent = new Intent(this, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // This clears all activities except Location, Login and Menu
            startActivity(intent);*/
        }
    }
}




