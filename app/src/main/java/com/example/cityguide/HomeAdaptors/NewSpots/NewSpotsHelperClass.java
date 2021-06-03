package com.example.cityguide.HomeAdaptors.NewSpots;

public class NewSpotsHelperClass {

    String name,description,imageUrl;
    float rating;
    int year,month,day;

    NewSpotsHelperClass(){}

    public NewSpotsHelperClass(String name,String description,String imageUrl,float rating,int year,int month,int day) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setRating(float rating) {
        this.rating = rating;
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

    public float getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
