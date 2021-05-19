package com.example.cityguide.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.cityguide.MainActivity;
import com.example.cityguide.R;
import com.google.firebase.auth.FirebaseAuth;

public class OTPVerify extends AppCompatActivity {

    ImageView CancelBtn;
    String phone, Whichscreen, match,match1,match2,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verify);

        phone = getIntent().getStringExtra("phoneS");
        userid = getIntent().getStringExtra("Userid");
        Whichscreen = getIntent().getStringExtra("whatToDo");
        match = "updatedata";
        match1 = "storedata";
        match2 = "changephone";


        CancelBtn = findViewById(R.id.cancelToSignup);
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().getCurrentUser().reload();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

}
