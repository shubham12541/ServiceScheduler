package com.entra.barbers.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shubham on 02/09/17.
 */

public class Shop implements Serializable{
    private String id, name, phone, address;
    private int seats, likes;
    private ArrayList<Service> sevices_m, services_f;
    private ArrayList<Review> reviews;
    private String lat, loc;
    ArrayList<Booking> bookings;

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }

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

    public ArrayList<Service> getSevices_m() {
        return sevices_m;
    }

    public void setSevices_m(ArrayList<Service> sevices_m) {
        this.sevices_m = sevices_m;
    }

    public ArrayList<Service> getServices_f() {
        return services_f;
    }

    public void setServices_f(ArrayList<Service> services_f) {
        this.services_f = services_f;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
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
