package com.example.cityguide.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cityguide.Common.Internet;
import com.example.cityguide.MainActivity;
import com.example.cityguide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassword extends AppCompatActivity {

    ImageView BackBtn;
    Button OtpButton;
    TextInputLayout phoneNumber;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        BackBtn = findViewById(R.id.backbtn_forgot);
        phoneNumber = findViewById(R.id.forgot_number);
        OtpButton = findViewById(R.id.forgot_nextButton);
        mProgressBar = findViewById(R.id.forgot_progressBar);

        BackBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Login.class)));
    }

    public void GetOTP(View view) {
        Internet object = new Internet();

        if (!object.Connected(this)) {
            showCustomDialog();
        }

        if (!ValidateFields()) {
            return;
        }

        String _phone = phoneNumber.getEditText().getText().toString().trim();

        mProgressBar.setVisibility(View.VISIBLE);
        OtpButton.setVisibility(View.INVISIBLE);


        String _email = phoneNumber.getEditText().getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("email").equalTo(_email);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    auth.sendPasswordResetEmail(_email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPassword.this, "Password reset email has been sent to you", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                                }
                            });

                    mProgressBar.setVisibility(View.GONE);
                    OtpButton.setVisibility(View.VISIBLE);

                } else {
                    mProgressBar.setVisibility(View.GONE);
                    OtpButton.setVisibility(View.VISIBLE);

                    phoneNumber.setError("No such user exist");
                    phoneNumber.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProgressBar.setVisibility(View.GONE);
                OtpButton.setVisibility(View.VISIBLE);
                Toast.makeText(ForgotPassword.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

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
}