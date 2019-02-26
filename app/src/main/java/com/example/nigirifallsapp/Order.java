package com.example.nigirifallsapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
//import org.jetbrains.annotations.NotNull;

public class Order implements Parcelable {
    private Integer customerID;
    private Integer orderID;
    //private List<Dish> dishes = new ArrayList<>();
    private Map<Dish, Integer> numOfEachDish;
    private int totalPrice;

    //public Order(@NotNull Menu menu, int orderID, int customerID){
    public Order(Menu menu, int orderID, int customerID){
        numOfEachDish = new HashMap<Dish, Integer>();
        //dishes = menu.getDishList();
        for (Dish dish : menu.getDishList()){
            numOfEachDish.put(dish, 0);
        }
        this.orderID = orderID;
        this.customerID = customerID;
    }

    protected Order(Parcel in) {
        if (in.readByte() == 0) {
            customerID = null;
        } else {
            customerID = in.readInt();
        }
        if (in.readByte() == 0) {
            orderID = null;
        } else {
            orderID = in.readInt();
        }
        totalPrice = in.readInt();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public void addDishToOrder(Dish newDish){
        int count = numOfEachDish.get(newDish);
        this.numOfEachDish.replace(newDish, count + 1);
    }

    public void removeDishInOrder(Dish newDish){
        int count = numOfEachDish.get(newDish);
        if (count > 0){
            this.numOfEachDish.replace(newDish, count - 1);
        }

    }

    public void updateTotalPrice(){
        int totPrice = 0;
        for (Dish dish : numOfEachDish.keySet()){

            totPrice += dish.price * numOfEachDish.get(dish);
        }
        this.totalPrice = totPrice;
    }

    public int getTotalPrice(){
        return this.totalPrice;
    }

    public Map<Dish, Integer> getNumOfEachDish(){
        return this.numOfEachDish;
    }

    //return "orderID|customerID|dishID1|numOfDish1|dishID2|numOfDish2|......|numOfDishN|totalPrice;
    public String getOrder(){
        StringBuilder sb = new StringBuilder();
        sb.append(orderID).append("|")
                .append(customerID).append("|");
        for (Dish dish : numOfEachDish.keySet()){
            sb.append(dish.dishID).append("|")
                    .append(numOfEachDish.get(dish)).append("|");
        }
        sb.append(totalPrice).append(";");
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (customerID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(customerID);
        }
        if (orderID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(orderID);
        }
        dest.writeInt(totalPrice);
    }
}