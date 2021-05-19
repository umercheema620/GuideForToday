package com.example.cityguide.HomeAdaptors.MostViewed;

public class MostViewedHelperClass {

    String name,description,imageUrl;

    MostViewedHelperClass(){}

    public MostViewedHelperClass(String name,String description,String imageUrl ) {
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
