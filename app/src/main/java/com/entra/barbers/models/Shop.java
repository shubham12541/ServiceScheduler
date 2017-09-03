package com.entra.barbers.models;

import java.io.Serializable;

/**
 * Created by shubham on 02/09/17.
 */

public class Shop implements Serializable{
    String id, name, phone, address;
    int seats, likes;
    String[] services_m, services_f;
    Review[] reviews;
    String lat, loc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String[] getServices_m() {
        return services_m;
    }

    public void setServices_m(String[] services_m) {
        this.services_m = services_m;
    }

    public String[] getServices_f() {
        return services_f;
    }

    public void setServices_f(String[] services_f) {
        this.services_f = services_f;
    }

    public Review[] getReviews() {
        return reviews;
    }

    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
