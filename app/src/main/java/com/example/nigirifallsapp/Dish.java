package com.example.nigirifallsapp;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Dish extends AppCompatActivity implements Parcelable {
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

    protected Dish(Parcel in) {
        dishID = in.readInt();
        name = in.readString();
        description = in.readString();
        price = in.readInt();
        minteger = in.readInt();
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dishID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(price);
        dest.writeInt(minteger);
    }
}
