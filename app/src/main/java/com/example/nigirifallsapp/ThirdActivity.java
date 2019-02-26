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
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = this.getIntent();
        ArrayList<Dish> order = intent.getParcelableArrayListExtra(MenuActivity.OrderIntent);

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
}
