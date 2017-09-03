package com.entra.barbers.models;

/**
 * Created by shubham on 02/09/17.
 */

public class User {
    String id, name, phone, gender;
    String[] likes;
    Review[] reviews;
    String[] bookings;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String[] getLikes() {
        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }

    public Review[] getReviews() {
        return reviews;
    }

    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
    }

    public String[] getBookings() {
        return bookings;
    }

    public void setBookings(String[] bookings) {
        this.bookings = bookings;
    }
}
