package com.example.nigirifallsapp;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private List<Dish> menu;

    public Menu(){
        menu = new ArrayList<>();
    }
    public void updateMenu(String importedMeny){

        List<Dish> updatedMenu = new ArrayList<Dish>();

        String[] dishes = importedMeny.split(";");

        for (String dish : dishes){
            String[] info = dish.split("\\|");
            int dishID = Integer.parseInt(info[0].trim());
            String name = info[1];
            String desc = info[2];
            int price = Integer.parseInt(info[3].trim());

            updatedMenu.add(new Dish(dishID,name,desc,price));
        }

        this.menu = updatedMenu;
    }
    public List<Dish> getDishList(){
        return this.menu;
    }
}

