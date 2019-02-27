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


import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    public static final String OrderIntent = "ThirdActivity.IntentString.Order";
    List<Dish> order;
    Button buttonPlaceOrder;
    Button buttonBackToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        this.buttonPlaceOrder = findViewById(R.id.button3);
        this.buttonBackToMenu = findViewById(R.id.returN);

        Intent intent = this.getIntent();
        ArrayList<Dish> order = intent.getParcelableArrayListExtra(MenuActivity.OrderIntent);

        // Function to add every Dish-item from ArrayList order to the ScrollView
        for (int i = 0; i < order.size(); i++) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dishView = layoutInflater.inflate(R.layout.dish_layout, null);

            TextView textName = dishView.findViewById(R.id.textName);
            TextView textDesc = dishView.findViewById(R.id.textDesc);
            TextView textPrice = dishView.findViewById(R.id.textPrice);

            textName.setText(order.get(i).getName());
            textDesc.setText(order.get(i).getDesc());
            textPrice.setText(Integer.toString(order.get(i).getPrice()));


            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.linearLayout);
            insertPoint.addView(dishView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        }



    }

    //go to remove or add items from checkout-menu
    public void onButtonBackToMenu(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        //intent.putParcelableArrayListExtra(OrderIntent, (ArrayList<? extends Parcelable>) this.order);
        startActivity(intent);


    }

    //transition to final state
    public void onButtonPlaceOrder(View view){
        Intent intent = new Intent(this, FinalActivity.class);
        //intent.putParcelableArrayListExtra(OrderIntent, (ArrayList<? extends Parcelable>) this.order);
        startActivity(intent);

    }

}
