package com.example.nigirifallsapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Dish extends AppCompatActivity {
    int dishID;
    String name;
    String description;
    int price;
    int minteger = 0;

    public Dish(int dishID, String name, String description, int price) {
        this.dishID = dishID;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getID() {
        return this.dishID;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.description;
    }

    public int getPrice() {
        return this.price;
    }


    public void decreaseInteger(View view) {
        minteger = minteger - 1;
        display(minteger);
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number);
        displayInteger.setText("" + number);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_layout);
    }

    public void increaseInteger(View view){
            minteger = minteger + 1;
            display(minteger);
        }
    }
