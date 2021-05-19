package com.example.cityguide.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityguide.Common.LoginSignup.Login;
import com.example.cityguide.Database.PlaceHelperClass;
import com.example.cityguide.Database.SessionManager;
import com.example.cityguide.HomeAdaptors.ProfileHelperClass;
import com.example.cityguide.MainActivity;
import com.example.cityguide.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    TextView name,email,number;
    DatabaseReference DataRef;
    String nameuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.profilename);
        email = findViewById(R.id.profileemail);
        number = findViewById(R.id.profilenumber);

        SessionManager session = new SessionManager(Profile.this);

        if(!session.checkLogin()) {
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show();
        }else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DataRef = FirebaseDatabase.getInstance().getReference().child("Users");
            nameuser = user.getUid();
            DataRef.child(nameuser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ProfileHelperClass profile = dataSnapshot.getValue(ProfileHelperClass.class);
                    if(profile != null) {
                        String acctname = profile.name;
                        String acctemail = profile.email;
                        String acctnumber = profile.phone;
                        name.setText(acctname);
                        email.setText(acctemail);
                        number.setText(acctnumber);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("error", databaseError.getMessage());
                }
            });
        }
    }

    public void onBackPressed(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void NameChange(View view) {
        Intent intent = new Intent(getApplicationContext(),ChangeName.class);
        intent.putExtra("userid",nameuser);
        startActivity(intent);
    }

    public void EmailChange(View view) {
        Intent intent = new Intent(getApplicationContext(),ChangeEmail.class);
        intent.putExtra("userid",nameuser);
        startActivity(intent);
    }

    public void PhoneChange(View view) {
        Intent intent = new Intent(getApplicationContext(),ChangePhone.class);
        intent.putExtra("userid",nameuser);
        startActivity(intent);
    }
}