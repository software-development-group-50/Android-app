package com.example.nigirifallsapp;
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
            int dishID = Integer.parseInt(info[1]);
            String name = info[2];
            String desc = info[3];
            int price = Integer.parseInt(info[4]);

            updatedMenu.add(new Dish(dishID,name,desc,price));
        }

        this.menu = updatedMenu;
    }
    public List<Dish> getDishList(){
        return this.menu;
    }
}

