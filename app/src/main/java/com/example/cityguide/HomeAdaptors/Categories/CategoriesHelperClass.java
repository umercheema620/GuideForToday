package com.example.cityguide.HomeAdaptors.Categories;

import android.graphics.drawable.GradientDrawable;

public class CategoriesHelperClass {

    int image;
    GradientDrawable gradient;
    String title,category;


    public CategoriesHelperClass(int image, GradientDrawable gradient, String title,String category) {
        this.image = image;
        this.gradient = gradient;
        this.title = title;
        this.category = category;
    }

    public int getImage() {
        return image;
    }

    public GradientDrawable getGradient() {
        return gradient;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }
}
