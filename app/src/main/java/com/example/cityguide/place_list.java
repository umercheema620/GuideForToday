package com.example.cityguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cityguide.HomeAdaptors.Featured.FeaturedHelperClass;
import com.example.cityguide.HomeAdaptors.Places.PlacesAdaptor;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class place_list extends AppCompatActivity {

    RecyclerView PlaceList;
    PlacesAdaptor PlaceListAdaptor;
    String comingfrom;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        //Hooks
        PlaceList = findViewById(R.id.list_view);
        PlaceList.setLayoutManager(new LinearLayoutManager(this));

        //Extra Information
        comingfrom = getIntent().getStringExtra("comingfrom");
        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);

        FirebaseRecyclerOptions<FeaturedHelperClass> options =
                new FirebaseRecyclerOptions.Builder<FeaturedHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(comingfrom), FeaturedHelperClass.class)
                        .build();
        PlaceListAdaptor = new PlacesAdaptor(options,latitude,longitude,place_list.this);
        PlaceList.setAdapter(PlaceListAdaptor);
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