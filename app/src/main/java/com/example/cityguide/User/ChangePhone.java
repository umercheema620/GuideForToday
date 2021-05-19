package com.example.cityguide.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cityguide.Common.Internet;
import com.example.cityguide.Common.LoginSignup.OTPVerify;
import com.example.cityguide.Common.LoginSignup.Signup3;
import com.example.cityguide.Common.LoginSignup.WelcomeScreen;
import com.example.cityguide.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class ChangePhone extends AppCompatActivity {

    CountryCodePicker country;
    TextInputLayout phoneNumber;
    ProgressBar mProgressBar;
    Button setphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);

        country = findViewById(R.id.change_phone_country);
        phoneNumber = findViewById(R.id.changephone);
        mProgressBar = findViewById(R.id.progressBarphone);
        setphone = findViewById(R.id.setphone);
    }

    public void OnBackPressed(View view) {
    }

    public void ChangePhone(View view) {
        Internet object = new Internet();

        if (!object.Connected(this)) {
            showCustomDialog();
        }

        if (!ValidateFields()) {
            return;
        }

        String _phone = phoneNumber.getEditText().getText().toString().trim();

        mProgressBar.setVisibility(View.VISIBLE);
        setphone.setVisibility(View.INVISIBLE);

        if (_phone.charAt(0) == '0') {
            _phone = _phone.substring(1);
        }

        String _completenumber = "+" + country.getSelectedCountryCode() + _phone;
        String userid = getIntent().getStringExtra("userid");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userid).child("phone").setValue(_completenumber);

        startActivity(new Intent(getApplicationContext(), Profile.class));
        finish();

    }

    private boolean ValidateFields() {

        String phone1 = phoneNumber.getEditText().getText().toString().trim();

        if (phone1.isEmpty()) {
            phoneNumber.setError("phone number cannot be empty");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }
    }

    public void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Please connect to the internet")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    startActivity(new Intent(getApplicationContext(), WelcomeScreen.class));
                    finish();
                });
        builder.show();
    }
}