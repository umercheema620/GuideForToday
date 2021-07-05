package com.example.cityguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PlaceData extends AppCompatActivity implements LocationListener {

    ImageView placeimage;
    CollapsingToolbarLayout name2;
    ChipNavigationBar mChipNavigationBar;
    String name, category, address, lat, lang;
    double latitude, longitude;
    double latitude1,longitude1;
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_data);

        placeimage = findViewById(R.id.placeimage);
        name2 = findViewById(R.id.collapsingbar);
        mChipNavigationBar = findViewById(R.id.chipnav);
        name = getIntent().getStringExtra("name");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Information(name)).commit();
        bottomMenu();

        String image = getIntent().getStringExtra("image");
        category = getIntent().getStringExtra("category");
        lat = getIntent().getStringExtra("latitude");
        lang = getIntent().getStringExtra("longitude");
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria cri = new Criteria();
        String provider = mLocationManager.getBestProvider(cri, false);
        if (provider != null && !provider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location1 = mLocationManager.getLastKnownLocation(provider);
            mLocationManager.requestLocationUpdates(provider,2000,1,this);
            if(location1 != null){
                onLocationChanged(location1);
            }
            else {
                Toast.makeText(this, "location not found", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Provider null", Toast.LENGTH_SHORT).show();
        }

        latitude = Double.parseDouble(lat);
        longitude = Double.parseDouble(lang);
        try {
            address = name2();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri image2 = Uri.parse(image);
        Glide.with(placeimage.getContext()).load(image2).into(placeimage);
        name2.setTitle(name);
    }

    private void bottomMenu() {
        mChipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.bottom_nav_highligt:
                        fragment = new Information(name);
                        break;
                    case R.id.bottom_nav_info:
                        fragment = new Highlights(name,address,category,latitude,longitude,latitude1,longitude1);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
    }

    public String name2() throws IOException {
        Geocoder geocoder = new Geocoder(PlaceData.this, Locale.getDefault());
        String loc1 = null;
        List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
        if(addresses != null && addresses.size() > 0){
            Address address = addresses.get(0);
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<address.getMaxAddressLineIndex();i++){
                sb.append(address.getAddressLine(i));
            }
            sb.append(address.getAddressLine(0));
            loc1 = sb.toString();
        }
        return loc1;
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude1 = location.getLatitude();
        longitude1 = location.getLongitude();
    }
}