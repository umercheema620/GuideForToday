package com.example.cityguide.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cityguide.Common.Internet;
import com.example.cityguide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeEmail extends AppCompatActivity {

    TextInputLayout changeemail;
    ProgressBar mProgressBar;
    Button setemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        changeemail = findViewById(R.id.change_email);
        mProgressBar = findViewById(R.id.progressBaremail);
        setemail = findViewById(R.id.setemail);
    }

    public void OnBackPressed(View view) {
        startActivity(new Intent(getApplicationContext(), Profile.class));
        finish();
    }

    public void ChangeEmail(View view) {

        Internet object = new Internet();

        if (!object.Connected(this)) {
            showCustomDialog();
        }

        if (!CheckEmail()) {
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        setemail.setVisibility(View.INVISIBLE);

        String _newEmail = changeemail.getEditText().getText().toString().trim();
        String userid = getIntent().getStringExtra("userid");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(_newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangeEmail.this, "Your email is updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userid).child("email").setValue(_newEmail);
        System.out.println("dlksjlfkdsjlkfjlksjd827438597");

        startActivity(new Intent(getApplicationContext(), Profile.class));
        finish();
    }

    public void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Please connect to the internet")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    finish();
                });
        builder.show();
    }

    private boolean CheckEmail() {
        String val = changeemail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            changeemail.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            changeemail.setError("Invalid Email!");
            return false;
        } else {
            changeemail.setError(null);
            changeemail.setErrorEnabled(false);
            return true;
        }
    }
}