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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = this.getIntent();
        //ArrayList<Dish> order = intent.getParcelableArrayListExtra(MenuActivity.OrderIntent);
        Order order = intent.getParcelableExtra(MenuActivity.OrderIntent);

        // Function to add every Dish-item from ArrayList order to the ScrollView
        //for (int i = 0; i < numOfEachDish.keySet().size(); i++) {
        for (Map.Entry<Dish, Integer> map : order.getNumOfEachDish().entrySet()){
            // Add if not == 0
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dishView = layoutInflater.inflate(R.layout.dish_layout, null);

            TextView textName = dishView.findViewById(R.id.textName);
            TextView textDesc = dishView.findViewById(R.id.textDesc);
            TextView textPrice = dishView.findViewById(R.id.textPrice);

            Dish dish = map.getKey();
            int price = dish.getPrice();
            textName.setText(dish.getName());
            textPrice.setText(map.getValue().toString()); // fix


            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.linearLayout);
            insertPoint.addView(dishView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        }

    }
}
