package com.example.cityguide.HomeAdaptors.Featured;

public class FeaturedHelperClass {

    String name,description,imageUrl,category,latitude,longitude;
    float rating;
    int year,month,day;

    FeaturedHelperClass(){}

    public FeaturedHelperClass(String name,String description,String imageUrl,String category,float rating,String latitude,String longitude,int year,int month,int day) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getCategory() {
        return category;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
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
