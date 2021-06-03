package com.example.cityguide.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cityguide.AddPlace;
import com.example.cityguide.Common.Internet;
import com.example.cityguide.Database.RememberSession;
import com.example.cityguide.Database.SessionManager;
import com.example.cityguide.MainActivity;
import com.example.cityguide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    ImageView BackBtn;
    TextInputLayout phone, password;
    TextInputEditText phone1, password1;
    Button loginButton;
    ProgressBar progressBar;
    CheckBox rememberme;
    String comingfrom;
    FirebaseAuth mAuth;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_login);

        BackBtn = findViewById(R.id.backbtn_login);
        phone = findViewById(R.id.login_number);
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.loginPage_Button);
        progressBar = findViewById(R.id.progressBar3);
        rememberme = findViewById(R.id.rememberMe);
        phone1 = findViewById(R.id.logintext_phone);
        password1 = findViewById(R.id.logintext_password);
        mAuth = FirebaseAuth.getInstance();
        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);

        comingfrom = getIntent().getStringExtra("comingfrom");

        BackBtn.setOnClickListener(view -> Login.super.onBackPressed());
        RememberSession sessionRem = new RememberSession(Login.this);
        if(sessionRem.checkRemember()){
            HashMap<String, String> userDetail = sessionRem.getRememberDetail();
            phone1.setText(userDetail.get(RememberSession.REMEMBER_PHONE));
            password1.setText(userDetail.get(RememberSession.REMEMBER_PASSWORD));
        }
    }

    public void ForgetPassword(View view) {
        startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
    }

    public void SignUp(View view) {
        Intent intent = new Intent(getApplicationContext(),Signup.class);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        startActivity(intent);
    }

    public void letUserLogIn(View view) {

        Internet object = new Internet();

        if(!object.Connected(this)){
            showCustomDialog();
        }

        if (!ValidateFields()) {
            return;
        }

        String _phone = phone.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);


        if(rememberme.isChecked()){
            RememberSession session = new RememberSession(Login.this);
            session.createRememberSession(_phone,_password);
        }

        mAuth.signInWithEmailAndPassword(_phone,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    SessionManager session = new SessionManager(Login.this);

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    FirebaseDatabase.getInstance().getReference("Users/" + userId).get().addOnSuccessListener(dataSnapshot -> {

                        Map<String, Object> data = (Map<String, Object>)dataSnapshot.getValue();
                        session.createLoginSession(userId, _phone, data.get("name").toString());

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    });

                }
                else{
                    progressBar.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                    Toast.makeText(Login.this, "Data does not exist!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean ValidateFields() {

        String phone1 = phone.getEditText().getText().toString().trim();
        String password1 = password.getEditText().getText().toString().trim();

        if (phone1.isEmpty()) {
            phone.setError("Email cannot be empty");
            return false;
        } else if (password1.isEmpty()) {
            password.setError("Password cannot be empty");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
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

}