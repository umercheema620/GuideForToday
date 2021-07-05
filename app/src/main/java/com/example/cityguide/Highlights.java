package com.example.cityguide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;

import java.io.IOException;
import java.util.Arrays;


public class Highlights extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String name,address,category;
    double latitude,longitude, lat, lang;

    TextView nametext;
    TextView addresstext;
    TextView categorytext;
    Button direction;
    RideRequestButton requestButton;
    private View view;

    public Highlights() {
    }

    public Highlights(String name, String address, String category, double latitude,double longitude,double lat, double lang) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lat = lat;
        this.lang= lang;
    }

    public static Highlights newInstance(String param1, String param2) {
        Highlights fragment = new Highlights();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highlights, container, false);
        this.view = view;

        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId("Z6v9AG1-GN9N-5hgin43RyIe0QY7U22a")
                .setClientSecret("O7Lc3_ophv7nRt_hnCGHGqagVkPSMJpIvqO-KgXP")
                .setEnvironment(SessionConfiguration.Environment.PRODUCTION)
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.REQUEST))
                .build();

        UberSdk.initialize(config);

        requestButton = view.findViewById(R.id.uber);

        RideParameters rideParams = new RideParameters.Builder()
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                .setDropoffLocation(
                        this.latitude, this.longitude, "Place", "1455 Market Street, San Francisco")
                .setPickupLocation(this.lat, this.lang, "Home", "1455 Market Street, San Francisco")
                .build();
        requestButton.setRideParameters(rideParams);

        nametext = view.findViewById(R.id.name);
        addresstext = view.findViewById(R.id.address);
        categorytext = view.findViewById(R.id.category);
        direction = view.findViewById(R.id.direction);

        nametext.setText(this.name);
        addresstext.setText(this.address);
        categorytext.setText(this.category);

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location2 = address;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+location2));
                startActivity(intent);
            }
        });

        return view;
    }

    public void GetDirections(View view) throws IOException {

    }
}