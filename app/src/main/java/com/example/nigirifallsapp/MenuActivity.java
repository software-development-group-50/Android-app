package com.example.nigirifallsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.widget.Button;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.util.List;
import java.io.Serializable;


public class MenuActivity extends AppCompatActivity {

    public static final String OrderIntent = "MenuActivity.IntentString.Order";
    public static final String HashMapIntent = "MenuActivity.IntentString.HashMap";
    Button buttonCheckout;
    RequestQueue requestQueue;
    String stringFromPHP;
    Menu menu;
    Order order;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_layout);
        this.requestQueue = Volley.newRequestQueue(this);
        this.sendRequest("http://folk.ntnu.no/magnuti/getalldish.php");
        this.buttonCheckout = findViewById(R.id.buttonCheckout);
        this.drawerLayout = findViewById(R.id.drawer_layout);

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

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        stringFromPHP = response;
        this.menu = new Menu();
        menu.updateMenu(stringFromPHP);
        this.order = new Order(menu, 1, 1);
        addMenuToView(this.menu);
    }

    // Function for adding each Dish-item to the ScrollView
    private void addMenuToView(Menu menu) { //Parameter Menu-class
        final List<Dish> tempDishList = menu.getDishList();
        for (int i = 0; i < tempDishList.size(); i++) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dishView = layoutInflater.inflate(R.layout.dish_layout, null);

            TextView textName = dishView.findViewById(R.id.textName);
            TextView textDesc = dishView.findViewById(R.id.textDesc);
            TextView textPrice = dishView.findViewById(R.id.textPrice);

            final Button buttonPluss = dishView.findViewById(R.id.buttonPluss);
            final Button buttonMinus = dishView.findViewById(R.id.buttonMinus);
            final TextView intnum = dishView.findViewById(R.id.integer_number);

            textName.setText(tempDishList.get(i).getName());
            textDesc.setText(tempDishList.get(i).getDesc());
            textPrice.setText(Integer.toString(tempDishList.get(i).getPrice()) + ",-");

            ImageView image = dishView.findViewById(R.id.dishImage);
            int imageID = getResources().getIdentifier("com.example.nigirifallsapp:drawable/dish_" + Integer.toString(i + 1), null, null);
            image.setImageResource(imageID);


            buttonPluss.setId(i);
            buttonMinus.setId(i);
            buttonPluss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddDish(tempDishList.get(buttonPluss.getId()));
                    //intnum.setText("counterValue");
                    //showValue = (TextView) findViewById(R.id.counterValue);
                    int count = Integer.valueOf(intnum.getText().toString());
                    intnum.setText(Integer.toString(count + 1));
                    changeTotalPrice();
                }
            });
            buttonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.valueOf(intnum.getText().toString());

                    if (count > 0) {
                        onRemoveDish(tempDishList.get(buttonMinus.getId()));
                        intnum.setText(Integer.toString(count - 1));
                        changeTotalPrice();

                    }
                }
            });

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.linearLayout);
            insertPoint.addView(dishView, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            // The -1 index specifies that the element is added LAST to the ScrollView
        }
    }

    // Function for moving to the Checkout-activity, the ArrayList this.order is passed to the Checkout-activity
    public void onButtonCheckout(View view) {
        if (order.getTotalPrice() > 0) {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra(OrderIntent, this.order);
            intent.putExtra(HashMapIntent, (Serializable) order.getNumOfEachDish());
            startActivity(intent);
        }
    }

    private void onAddDish(Dish dish) {
        this.order.addDishToOrder(dish);
    }

    private void onRemoveDish(Dish dish) {
        try {
            this.order.removeDishInOrder(dish);
        } catch (Exception e) {

        }
    }

    // This function is called on the hardware back-button on the phone.
    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
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
    }

    private void logOut() {
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        sp.edit().putBoolean("logged", false).apply();
        NavUtils.navigateUpFromSameTask(this); // This clears the Menu activity from the activity stack. Only Login activity now.
    }

    private void changeTotalPrice() {
        Integer totalPrice = this.order.getTotalPrice();
        Button checkoutButton = (Button) findViewById(R.id.buttonCheckout);
        String price = Integer.toString(totalPrice);
        if (price.equals("0")) {
            checkoutButton.setText("Shopping Cart is empty");
        } else {
            checkoutButton.setText("Proceed to checkout: " + price + ";-");
        }
    }
}
