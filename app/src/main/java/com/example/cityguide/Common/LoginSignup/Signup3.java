package com.example.cityguide.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cityguide.Common.Internet;
import com.example.cityguide.Database.UserHelperClass;
import com.example.cityguide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class Signup3 extends AppCompatActivity {

    ImageView BackBtn;
    Button OtpButton;
    CountryCodePicker country;
    TextInputLayout phoneNumber;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        progressBar = findViewById(R.id.progressBar);
        BackBtn = findViewById(R.id.backbtn_signup3);
        phoneNumber = findViewById(R.id.number);
        country = findViewById(R.id.country1);
        OtpButton = findViewById(R.id.signup3_next);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup3.super.onBackPressed();
            }
        });

    }

    public void GetOTPSign(View v){

        Internet object = new Internet();

        if(!object.Connected(this)){
            showCustomDialog();
        }

        if (!CheckPhoneNumber()) {
            return;
        }


        String phone = phoneNumber.getEditText().getText().toString().trim();

        if (phone.charAt(0) == '0') {
            phone = phone.substring(1);
        }

        String phonesignup = "+" + country.getSelectedCountryCode() + phone;


        String namesignup = getIntent().getStringExtra("nameS");
        String usersignup = getIntent().getStringExtra("userS");
        String emailsignup = getIntent().getStringExtra("emailS");
        String passwordsignup = getIntent().getStringExtra("passwordS");
        String gendersignup = getIntent().getStringExtra("genderS");
        String datesignup = getIntent().getStringExtra("dateS");

        progressBar.setVisibility(View.VISIBLE);
        OtpButton.setVisibility(View.INVISIBLE);

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailsignup,passwordsignup).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                DatabaseReference rootNode = FirebaseDatabase.getInstance().getReference("Users");

                UserHelperClass AddNewUser = new UserHelperClass(namesignup, usersignup, passwordsignup, emailsignup, phonesignup, gendersignup, datesignup);
                rootNode.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(AddNewUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signup3.this, "New User Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(), OTPVerify.class));
                                    Toast.makeText(Signup3.this, "Email verification sent", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Signup3.this, "There was a problem", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    public void showCustomDialog(){
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

    private boolean CheckPhoneNumber() {
        String val = phoneNumber.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            phoneNumber.setError("Enter valid phone number");
            return false;
        } else if (!val.matches(checkspaces)) {
            phoneNumber.setError("No White spaces are allowed!");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }
    }

}