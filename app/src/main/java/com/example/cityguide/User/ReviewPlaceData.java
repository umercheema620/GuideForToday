package com.example.cityguide.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cityguide.Database.PlaceHelperClass;
import com.example.cityguide.HomeAdaptors.ProfileHelperClass;
import com.example.cityguide.MainActivity;
import com.example.cityguide.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class ReviewPlaceData extends AppCompatActivity {

    TextView name,desc;
    ImageView image;
    String Pname,Pdesc,Pimage;
    String nameuser;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Review");
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_place_data);
        nameuser = user.getUid();

        name = findViewById(R.id.reviewname);
        desc = findViewById(R.id.reviewdesc);
        image = findViewById(R.id.reviewimage);

        Pname = getIntent().getStringExtra("name");
        Pdesc = getIntent().getStringExtra("des");
        Pimage = getIntent().getStringExtra("image");

        name.setText(Pname);
        desc.setText(Pdesc);
        Glide.with(image.getContext()).load(Pimage).into(image);

    }

    public void GotoMain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void Approve(View view) {

        ref.child(Pname).child("approve").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer score = currentData.getValue(Integer.class);
                if (score == null) {
                    return Transaction.success(currentData);
                }
                if (score != null){
                    currentData.setValue(score + 1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
            }
        });

        ref.child(Pname).child("voted").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer score = currentData.getValue(Integer.class);
                if (score == null) {
                    return Transaction.success(currentData);
                }
                if (score != null){
                    currentData.setValue(score + 1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
            }
        });

        ref2.child(nameuser).child("notification").child(Pname).removeValue();
        Toast.makeText(this, "Place Approved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),Notification.class));
        finish();
    }

    public void DisApprove(View view) {
        ref.child(Pname).child("disapprove").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer score = currentData.getValue(Integer.class);
                if (score == null) {
                    return Transaction.success(currentData);
                }
                if (score != null){
                    currentData.setValue(score + 1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
            }
        });

        ref.child(Pname).child("voted").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer score = currentData.getValue(Integer.class);
                if (score == null) {
                    return Transaction.success(currentData);
                }
                if (score != null){
                    currentData.setValue(score + 1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
            }
        });

        ref2.child(nameuser).child("notification").child(Pname).removeValue();
        Toast.makeText(this, "Place Not Approved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),Notification.class));
        finish();
    }
}