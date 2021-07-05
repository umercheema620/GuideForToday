package com.example.cityguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cityguide.HomeAdaptors.Featured.FeaturedHelperClass;
import com.example.cityguide.HomeAdaptors.Places.PlacesAdaptor2;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Featured_list extends AppCompatActivity {

    RecyclerView PlaceList;
    PlacesAdaptor2 PlaceListAdaptor;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_list);

        PlaceList = findViewById(R.id.featured_list_view);
        PlaceList.setLayoutManager(new LinearLayoutManager(this));

        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);

        FirebaseRecyclerOptions<FeaturedHelperClass> options =
                new FirebaseRecyclerOptions.Builder<FeaturedHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Places"),FeaturedHelperClass.class)
                        .build();
        PlaceListAdaptor = new PlacesAdaptor2(options,latitude,longitude,Featured_list.this);
        PlaceList.setAdapter(PlaceListAdaptor);
    }

    public void Back(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PlaceListAdaptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        PlaceListAdaptor.stopListening();
    }
}