package com.example.cityguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.cityguide.HomeAdaptors.Featured.FeaturedHelperClass;
import com.example.cityguide.HomeAdaptors.Places.PlacesAdaptor;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class place_list extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    RecyclerView PlaceList;
    PlacesAdaptor PlaceListAdaptor;
    String comingfrom;
    Query query;
    String[] category = { "","Gaming", "Restaurant", "Gym", "Monument","Cinema", "Mosque", "Park", "Ice-cream Parlor","hospital", "other"};

    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        //Hooks
        PlaceList = findViewById(R.id.list_view);
        PlaceList.setLayoutManager(new LinearLayoutManager(this));
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Extra Information
        comingfrom = getIntent().getStringExtra("comingfrom");
        category[0]=comingfrom;
        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,category);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        FirebaseRecyclerOptions<FeaturedHelperClass> options =
                new FirebaseRecyclerOptions.Builder<FeaturedHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(comingfrom), FeaturedHelperClass.class)
                        .build();
        PlaceListAdaptor = new PlacesAdaptor(options,latitude,longitude,place_list.this);
        PlaceList.setAdapter(PlaceListAdaptor);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){
            query = FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(category[position]);
        }else if(position == 1){
            query = FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(category[position]);
        }
        else if(position == 2){
            query = FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(category[position]);
        }
        else if(position == 3){
            query = FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(category[position]);
        }
        else if(position == 4){
            query = FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(category[position]);
        }
        else if(position == 5){
            query = FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(category[position]);
        }
        else if(position == 6){
            query = FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(category[position]);
        }
        else if(position == 7){
            query = FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(category[position]);
        }
        else if(position == 8){
            query = FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(category[position]);
        }
        else if(position == 9){
            query = FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(category[position]);
        }
        else if(position == 10){
            query = FirebaseDatabase.getInstance().getReference("Places").orderByChild("category").equalTo(category[position]);
        }
        FirebaseRecyclerOptions<FeaturedHelperClass> options =
                new FirebaseRecyclerOptions.Builder<FeaturedHelperClass>()
                        .setQuery(query, FeaturedHelperClass.class)
                        .build();
        PlaceListAdaptor.updateOptions(options);
        PlaceList.setAdapter(PlaceListAdaptor);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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