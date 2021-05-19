package com.example.cityguide.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.cityguide.Common.CategoriesFunction;
import com.example.cityguide.R;

public class Categories extends AppCompatActivity {

    ImageView BackBtn;
    double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        //Hooks

        BackBtn = findViewById(R.id.back_pressed);

        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Categories.super.onBackPressed();
            }
        });
    }

    public void GetMonumentList(View view){
        CategoriesFunction monument = new CategoriesFunction(this,latitude,longitude);
        monument.Monuments();
    }

    public void GetRestaurantList(View view){
        CategoriesFunction restaurant = new CategoriesFunction(this,latitude,longitude);
        restaurant.Restaurant();
    }

    public void GetMosqueList(View view){
        CategoriesFunction mosque = new CategoriesFunction(this,latitude,longitude);
        mosque.Mosque();
    }

    public void GetGamingList(View view){
        CategoriesFunction gaming = new CategoriesFunction(this,latitude,longitude);
        gaming.Gaming();
    }

    public void GetCinemaList(View view){
        CategoriesFunction cinema = new CategoriesFunction(this,latitude,longitude);
        cinema.Cinema();
    }

    public void GetGymList(View view){
        CategoriesFunction gym = new CategoriesFunction(this,latitude,longitude);
        gym.Gym();
    }

}