package com.example.cityguide.Database;

import java.time.LocalDate;

public class PlaceHelperClass {
    private String name,category,location,imageUrl,description, latitude,longitude,user;
    private int year,month,day;
    public int approve,disapprove,voted;
    boolean featured;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public PlaceHelperClass(){}

    public PlaceHelperClass(String name, String description,String imageUrl,String category, String location, String latitude, String longitude,String user, int year, int month, int day,int approve, int disapprove, int voted, boolean featured) {
        this.name = name;
        this.category = category;
        this.location = location;
        this.imageUrl = imageUrl;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
        this.year = year;
        this.month = month;
        this.day = day;
        this.approve = approve;
        this.disapprove = disapprove;
        this.voted = voted;
        this.featured = featured;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public int getApprove() {
        return approve;
    }

    public void setApprove(int approve) {
        this.approve = approve;
    }

    public int getDisapprove() {
        return disapprove;
    }

    public void setDisapprove(int disapprove) {
        this.disapprove = disapprove;
    }

    public int getVoted() {
        return voted;
    }

    public void setVoted(int voted) {
        this.voted = voted;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image) {
        this.imageUrl = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}