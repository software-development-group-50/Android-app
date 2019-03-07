package com.example.nigirifallsapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Order implements Parcelable {
    private int customerID;
    private int orderID;
    private Map<Dish, Integer> numOfEachDish;
    //private int totalPrice;

    //public Order(@NotNull Menu menu, int orderID, int customerID){
    public Order(Menu menu, int orderID, int customerID) {
        //dishes = menu.getDishList();
        this.numOfEachDish = new HashMap<>();
        for (Dish dish : menu.getDishList()) {
            numOfEachDish.put(dish, 0);
        }
        this.orderID = orderID;
        this.customerID = customerID;
    }

    protected Order(Parcel in) {
        customerID = in.readInt();
        orderID = in.readInt();
        //totalPrice = in.readInt();
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

    public void addDishToOrder(Dish newDish) {
        int count = numOfEachDish.get(newDish);
        this.numOfEachDish.remove(newDish);
        this.numOfEachDish.put(newDish, count + 1);
    }

    public void removeDishInOrder(Dish newDish) {
        int count = numOfEachDish.get(newDish);
        if (count > 0) {
            this.numOfEachDish.remove(newDish);
            this.numOfEachDish.put(newDish, count - 1);
        }

    }

    public void updateTotalPrice() {
        int totPrice = 0;
        for (Dish dish : numOfEachDish.keySet()) {

            totPrice += dish.price * numOfEachDish.get(dish);
        }
        //this.totalPrice = totPrice;
    }

    /*public int getTotalPrice(){
        return this.totalPrice;
    }*/

    public Map<Dish, Integer> getNumOfEachDish() {
        return this.numOfEachDish;
    }

    //return "http://folk.ntnu.no/jennyjy/addorder.php/?order=dishID1|numOfDish1|dishID2|numOfDish2|......|numOfDishN;
    public String getOrder() {
        StringBuilder sb = new StringBuilder();
        //sb.append(orderID).append("|")
        //        .append(customerID).append("|");
        //for (Dish dish : numOfEachDish.keySet()){
        for (Map.Entry<Dish, Integer> entry : numOfEachDish.entrySet()) {
            sb.append(entry.getKey().dishID).append("|")
                    .append(entry.getValue()).append("|");
        }
        //sb.append(totalPrice).append(";");
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(customerID);
        dest.writeInt(orderID);
    }
}