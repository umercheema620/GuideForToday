package com.example.cityguide.Common;

import android.content.Context;
import android.content.Intent;

import com.example.cityguide.place_list;


public class CategoriesFunction {

    private Context mContext;
    double latitude,longitude;
    public CategoriesFunction(Context context,double latitude, double longitude){
        this.mContext = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void Restaurant(){
        String var = "Restaurant";
        Intent intent = new Intent(mContext, place_list.class);
        intent.putExtra("comingfrom",var);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        mContext.startActivity(intent);
    }

    public void Monuments(){
        String var = "Monument";
        Intent intent = new Intent(mContext, place_list.class);
        intent.putExtra("comingfrom",var);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        mContext.startActivity(intent);
    }

    public void Mosque(){
        String var = "Mosque";
        Intent intent = new Intent(mContext, place_list.class);
        intent.putExtra("comingfrom",var);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        mContext.startActivity(intent);
    }

    public void Gaming(){
        String var = "Gaming";
        Intent intent = new Intent(mContext, place_list.class);
        intent.putExtra("comingfrom",var);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        mContext.startActivity(intent);
    }

    public void Cinema(){
        String var = "Cinema";
        Intent intent = new Intent(mContext, place_list.class);
        intent.putExtra("comingfrom",var);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        mContext.startActivity(intent);
    }

    public void Gym(){
        String var = "Gym";
        Intent intent = new Intent(mContext, place_list.class);
        intent.putExtra("comingfrom",var);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        mContext.startActivity(intent);
    }
}
