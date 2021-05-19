package com.example.cityguide.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cityguide.MainActivity;
import com.example.cityguide.R;
import com.google.android.material.textfield.TextInputLayout;

public class Signup extends AppCompatActivity {

    ImageView BackBtn;
    TextView signupText;
    Button nextButton, loginbtn;

    TextInputLayout name, username, email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_signup);

        BackBtn = findViewById(R.id.backbtn_signup1);
        signupText = findViewById(R.id.signup1_title);
        nextButton = findViewById(R.id.signup1_next);
        loginbtn = findViewById(R.id.signup1_login);

        name = findViewById(R.id.signup_name);
        username = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WelcomeScreen.class));
                finish();
            }
        });

    }

    public void CallNextSignup(View view) {

        if (!CheckName() | !CheckUsername() | !CheckPassword() | !CheckEmail()) {
            return;
        }

        String namesignup = name.getEditText().getText().toString().trim();
        String usersignup = username.getEditText().getText().toString().trim();
        String passwordsignup = password.getEditText().getText().toString().trim();
        String emailsignup = email.getEditText().getText().toString().trim();

        Intent intent = new Intent(getApplicationContext(), Signup2.class);

        intent.putExtra("nameS", namesignup);
        intent.putExtra("userS", usersignup);
        intent.putExtra("emailS", emailsignup);
        intent.putExtra("passwordS",passwordsignup);

        Pair[] pairs = new Pair[4];

        pairs[0] = new Pair<View, String>(BackBtn, "transition_signup1_back_btn");
        pairs[1] = new Pair<View, String>(signupText, "transition_signup1_title_text");
        pairs[2] = new Pair<View, String>(nextButton, "transition_signup1_next_btn");
        pairs[3] = new Pair<View, String>(loginbtn, "transition_signup1_login_btn");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup.this, pairs);
            startActivity(intent, options.toBundle());
            finish();
        } else {
            startActivity(intent);
            finish();
        }

    }

    public boolean CheckName() {

        String val = name.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            name.setError("Field cannot be empty");
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    public boolean CheckUsername() {

        String val = username.getEditText().getText().toString().trim();
        String CheckSpaces = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username cannot exceed 20 characters");
            return false;
        } else if (!val.matches(CheckSpaces)) {
            username.setError("No Whitespaces Allowed");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean CheckEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean CheckPassword() {
        String val = password.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else if (val.length() < 8) {
            password.setError("Password must be 8 characters long");
            return false;
        } else if (!val.matches("(.*[A-Z].*)")) {
            password.setError("Password must contain Uppercase");
            return false;
        } else if (!val.matches("(.*[0-9].*)")) {
            password.setError("Password must contain Numbers");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}