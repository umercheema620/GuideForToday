package com.example.cityguide.HomeAdaptors.Featured;

public class FeaturedHelperClass {

    String name,description,imageUrl;

    FeaturedHelperClass(){}

    public FeaturedHelperClass(String name,String description,String imageUrl ) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
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
