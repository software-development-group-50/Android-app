package com.example.nigirifallsapp;


public class Dish {
    int dishID;
    String name;
    String description;
    int price;

    public Dish(int dishID, String name, String description, int price){
        this.dishID = dishID;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public int getID(){
        return this.dishID;
    }
    public String getName(){
        return this.name;
    }
    public String getDesc(){
        return this.description;
    }
    public int getPrice(){
        return this.price;
    }
}
