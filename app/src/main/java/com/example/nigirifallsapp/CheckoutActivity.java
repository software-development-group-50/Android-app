package com.example.nigirifallsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    Integer orderID;
    private DrawerLayout drawerLayout;


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
                insertPoint.addView(dishView, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                // The -1 index specifies that the element is added LAST to the ScrollView
            }
        }
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
        Intent intent = new Intent(this, PickupActivity.class);
        String url = "http://org.ntnu.no/nigiriapp/addorder.php/?order=";
        url += orderHashMapToString();
        intent.putExtra(this.OrderIntent, url);
        startActivity(intent);
    }

    // This function is called on the hardware back-button on the phone.
    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
