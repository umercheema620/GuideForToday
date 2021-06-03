package com.example.cityguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.cityguide.Common.Directions;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;

import java.util.Arrays;

public class Search extends AppCompatActivity {

    RideRequestButton requestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SessionConfiguration config = new SessionConfiguration.Builder()
                .setClientId("Z6v9AG1-GN9N-5hgin43RyIe0QY7U22a")
                .setClientSecret("O7Lc3_ophv7nRt_hnCGHGqagVkPSMJpIvqO-KgXP")
                .setEnvironment(SessionConfiguration.Environment.PRODUCTION)
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.REQUEST))
                .build();

        UberSdk.initialize(config);

        requestButton = findViewById(R.id.uber);

        RideParameters rideParams = new RideParameters.Builder()
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                .setDropoffLocation(
                        31.58805725639512, 74.31072883307935, "Place", "1455 Market Street, San Francisco")
                .setPickupLocation(31.368616, 74.1782821, "Home", "1455 Market Street, San Francisco")
                .build();
        requestButton.setRideParameters(rideParams);

    }

    public void GetDirections(View view) {
        startActivity(new Intent(getApplicationContext(), Directions.class));
    }
}