package com.example.cityguide.HomeAdaptors.WishList;

public class WishListHelperClass {

    String name,description,imageUrl;

    WishListHelperClass(){}

    public WishListHelperClass(String name,String description,String imageUrl ) {
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
