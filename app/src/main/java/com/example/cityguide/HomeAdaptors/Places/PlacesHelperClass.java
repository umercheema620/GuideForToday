package com.example.cityguide.HomeAdaptors.Places;

public class PlacesHelperClass {

    String name,description,imageUrl,latitude,longitude;

    PlacesHelperClass(){}

    public PlacesHelperClass(String name,String description,String imageUrl,String latitude, String longitude) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
