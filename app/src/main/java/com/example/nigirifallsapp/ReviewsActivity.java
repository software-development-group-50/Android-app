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

public class ReviewsActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    Button buttonConfirm;
    Button buttonFinish;
    LinearLayout linearLayoutReview;
    int reviewIndex;
    int reviewID_;
    private int defaultColor;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        this.requestQueue = Volley.newRequestQueue(this);

        this.linearLayoutReview = findViewById(R.id.linearLayoutReviewOuter);
        this.drawerLayout = findViewById(R.id.drawer_layout);
        //setTitle("Orders for " + this.sharedPreferences.getString("locationString", "error"));
        setTitle("User Reviews");
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
        this.sendRequestGetAllOrders("http://org.ntnu.no/nigiriapp/getreviews.php");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.nav_admin_orders:
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_admin_reviews:
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
        List<Review> reviewList = new ArrayList<>();
        String[] arrayWithStringReviews = response.split(";");
        for (String elementsInStringArray : arrayWithStringReviews) {
            Review newReview = new Review(elementsInStringArray);
            reviewList.add(newReview);
        }
        addMenuToView(reviewList);
    }

    // Function for adding all orders to the ScrollView and adding ClickListeners to them
    private void addMenuToView(List<Review> reviewList) {
        // Reversed for-loop, since we want newest order on top
        for (int i = reviewList.size() - 1; i >= 0; i--) {
            LayoutInflater outerLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View reviewView = outerLayoutInflater.inflate(R.layout.review_layout, null);

            final TextView textOrders = reviewView.findViewById(R.id.reviewOrderID);
            final TextView stars = reviewView.findViewById(R.id.reviewStars);
            final TextView comment = reviewView.findViewById(R.id.reviewComment);

            textOrders.setText(reviewList.get(i).getOrderId());
            stars.setText(reviewList.get(i).getStars());
            comment.setText(reviewList.get(i).getComment());

            ViewGroup outerInsertPoint = (ViewGroup) findViewById(R.id.linearLayoutReviewOuter);
            System.out.println(outerInsertPoint);
            outerInsertPoint.addView((reviewView), -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            //Index -1 -> older orders are placed to the bottom
        }
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
    }
}

