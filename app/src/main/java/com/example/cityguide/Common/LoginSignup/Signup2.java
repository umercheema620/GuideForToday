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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityguide.R;

import java.util.Calendar;

public class Signup2 extends AppCompatActivity {

    ImageView BackBtn;
    TextView signupText;
    Button nextButton, loginbtn;
    double latitude,longitude;
    RadioGroup radioGroup;
    DatePicker datePicker;
    RadioButton selectGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        BackBtn = findViewById(R.id.backbtn_signup2);
        signupText = findViewById(R.id.signup2_title);
        nextButton = findViewById(R.id.signup2_next);
        loginbtn = findViewById(R.id.signup2_login);

        radioGroup = findViewById(R.id.radio_signup);
        datePicker = findViewById(R.id.date_signup);
        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);


        BackBtn.setOnClickListener(view -> Signup2.super.onBackPressed());
    }

    public void CallnextSignup(View view) {

        if(!CheckAge() | !CheckGender()){
            return;
        }

        selectGender = findViewById(radioGroup.getCheckedRadioButtonId());
        String gendersignup = selectGender.getText().toString();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        String datesignup = day+"/"+month+"/"+year;

        String namesignup = getIntent().getStringExtra("nameS");
        String usersignup = getIntent().getStringExtra("userS");
        String emailsignup = getIntent().getStringExtra("emailS");
        String passwordsignup = getIntent().getStringExtra("passwordS");

        Intent intent = new Intent(getApplicationContext(), Signup3.class);

        intent.putExtra("nameS", namesignup);
        intent.putExtra("userS", usersignup);
        intent.putExtra("emailS", emailsignup);
        intent.putExtra("passwordS",passwordsignup);
        intent.putExtra("genderS", gendersignup);
        intent.putExtra("dateS", datesignup);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);

        Pair[] pairs = new Pair[4];

        pairs[0] = new Pair<View, String>(BackBtn, "transition_signup3_back_btn");
        pairs[1] = new Pair<View, String>(signupText, "transition_signup3_title_text");
        pairs[2] = new Pair<View, String>(nextButton, "transition_signup3_next_btn");
        pairs[3] = new Pair<View, String>(loginbtn, "transition_signup3_login_btn");


        startActivity(intent);
        finish();

    }

    private boolean CheckGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean CheckAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 14) {
            Toast.makeText(this, "You are not eligible to apply", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }
}