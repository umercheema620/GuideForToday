package com.example.cityguide.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.cityguide.AddPlace;
import com.example.cityguide.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class MapShow extends AppCompatActivity implements OnMapReadyCallback {

    MapView map;
    LatLng target;
    double latitude;
    double longitude;
    private double LAT, LON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);

        map = findViewById(R.id.map_view1);
        latitude = getIntent().getDoubleExtra("latitude1",0);
        longitude = getIntent().getDoubleExtra("longitude1",0);

        map.getMapAsync(this);
        map.onCreate(savedInstanceState);


    }


    public void GetLocation(View view) {
        String placename = getIntent().getStringExtra("nameP");
        String placecategory = getIntent().getStringExtra("categoryP");
        String placedescription = getIntent().getStringExtra("DescriptionP");
        Intent intent = new Intent(getApplicationContext(), AddPlace.class);
        intent.putExtra("locationP", String.valueOf(target));
        intent.putExtra("latitude", String.valueOf(LAT));
        intent.putExtra("longitude", String.valueOf(LON));
        intent.putExtra("nameP", String.valueOf(placename));
        intent.putExtra("categoryP", String.valueOf(placecategory));
        intent.putExtra("DescriptionP", String.valueOf(placedescription));
        startActivity(intent);
        Toast.makeText(this, "Location Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final LatLng YourLocation = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(YourLocation, 18.0f));

        googleMap.setOnCameraMoveListener(() -> {
            target = googleMap.getCameraPosition().target;
            LAT = target.latitude;
            LON = target.longitude;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        map.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }



}

