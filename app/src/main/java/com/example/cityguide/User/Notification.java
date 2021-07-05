package com.example.cityguide.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cityguide.Database.NotificationAdaptor;
import com.example.cityguide.Database.PlaceHelperClass;
import com.example.cityguide.MainActivity;
import com.example.cityguide.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Notification extends AppCompatActivity {

    RecyclerView notifications;
    NotificationAdaptor mAdaptor;
    String nameuser;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        nameuser = user.getUid();

        notifications = findViewById(R.id.notification_view);
        notifications.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<PlaceHelperClass> options =
                new FirebaseRecyclerOptions.Builder<PlaceHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(nameuser).child("notification").orderByChild("name"),PlaceHelperClass.class)
                        .build();
        mAdaptor = new NotificationAdaptor(options,this);
        notifications.setAdapter(mAdaptor);
    }

    public void Back(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdaptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdaptor.stopListening();
    }
}