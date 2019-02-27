package com.example.nigirifallsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


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
        Order order = intent.getParcelableExtra(MenuActivity.OrderIntent);
        Map<Dish, Integer> numOfEachDish = (HashMap<Dish, Integer>) intent.getSerializableExtra(MenuActivity.HashMapIntent);


        // Function to add every Dish-item from ArrayList order to the ScrollView
        for (Map.Entry<Dish, Integer> map : numOfEachDish.entrySet()){
            if(map.getValue() != 0) {
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
