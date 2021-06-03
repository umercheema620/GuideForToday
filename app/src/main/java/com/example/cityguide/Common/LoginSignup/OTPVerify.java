package com.example.cityguide.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.example.cityguide.MainActivity;
import com.example.cityguide.R;
import com.google.firebase.auth.FirebaseAuth;

public class OTPVerify extends AppCompatActivity {

    ImageView CancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verify);

        CancelBtn = findViewById(R.id.cancelToSignup);

        CancelBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().getCurrentUser().reload();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }

}
