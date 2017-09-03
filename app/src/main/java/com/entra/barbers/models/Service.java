package com.entra.barbers.models;

import java.io.Serializable;

/**
 * Created by shubham on 03/09/17.
 */

public class Service implements Serializable{
    String name;
    int time_mins, price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime_mins() {
        return time_mins;
    }

    public void setTime_mins(int time_mins) {
        this.time_mins = time_mins;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
