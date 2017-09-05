package com.entra.barbers.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shubham on 03/09/17.
 */

public class Order implements Serializable{
    String orderId, userId, timestamp, startTime, endTime;
    int amount, time_mins;
    ArrayList<String> services;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
        this.services = services;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTime_mins() {
        return time_mins;
    }

    public void setTime_mins(int time_mins) {
        this.time_mins = time_mins;
    }
}
