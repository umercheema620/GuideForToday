package com.example.cityguide.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.cityguide.Common.Internet;
import com.example.cityguide.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeName extends AppCompatActivity {

    TextInputLayout changename;
    ProgressBar mProgressBar;
    Button setname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        changename = findViewById(R.id.change_name);
        mProgressBar = findViewById(R.id.progressBarname);
        setname = findViewById(R.id.setname);
    }

    public void ChangeName(View view) {
        Internet object = new Internet();

        if (!object.Connected(this)) {
            showCustomDialog();
        }

        if (!CheckName()) {
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        setname.setVisibility(View.INVISIBLE);

        String _newName = changename.getEditText().getText().toString().trim();
        String userid = getIntent().getStringExtra("userid");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userid).child("name").setValue(_newName);

        startActivity(new Intent(getApplicationContext(), Profile.class));
        finish();
    }

    public void OnBackPressed(View view) {
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

    public boolean CheckName() {

        String val = changename.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            changename.setError("Field cannot be empty");
            return false;
        } else {
            changename.setError(null);
            changename.setErrorEnabled(false);
            return true;
        }
    }

}