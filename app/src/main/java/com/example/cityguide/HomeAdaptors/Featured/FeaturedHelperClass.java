package com.example.cityguide.HomeAdaptors.Featured;

public class FeaturedHelperClass {

    String name,description,imageUrl;
    float rating;

    FeaturedHelperClass(){}

    public FeaturedHelperClass(String name,String description,String imageUrl,float rating) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.rating = rating;
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
