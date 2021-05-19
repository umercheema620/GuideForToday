package com.example.cityguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.cityguide.OnboardingClasses.onBoardingFragment1;
import com.example.cityguide.OnboardingClasses.onBoardingFragment2;
import com.example.cityguide.OnboardingClasses.onBoardingFragment3;
import com.example.cityguide.OnboardingClasses.onBoardingFragment4;

import org.jetbrains.annotations.NotNull;

public class Intro_Activity extends AppCompatActivity {

    private static int SPLASH_TIMER = 5000;
    LottieAnimationView lottieanimationsplash;
    TextView splashAppname;
    Animation anim;
    SharedPreferences onBoardingScreenPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_);

        splashAppname = findViewById(R.id.app_name);
        lottieanimationsplash = findViewById(R.id.lottie);

        splashAppname.animate().translationY(-1800).setDuration(1000).setStartDelay(4000);
        lottieanimationsplash.animate().translationY(1800).setDuration(1000).setStartDelay(4000);

        if(restorePrefData()){
            new Handler().postDelayed(()->{
                Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainIntent);
                finish();
            },SPLASH_TIMER);

        }
        else{
            new Handler().postDelayed(()->{
            ViewPager viewpager = findViewById(R.id.pager);
            ScreenAdaptor pagerAdopter = new ScreenAdaptor(getSupportFragmentManager());
            viewpager.setAdapter(pagerAdopter);

            anim = AnimationUtils.loadAnimation(this,R.anim.slider_animation);
            viewpager.startAnimation(anim);
            },SPLASH_TIMER);
        }

    }

    private boolean restorePrefData() {
        onBoardingScreenPref = getApplicationContext().getSharedPreferences("onBoardingPref",MODE_PRIVATE);
        return onBoardingScreenPref.getBoolean("isoNboardOpened",false);
    }

    public void skip(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        savePrefsData();
        finish();
    }

    private void savePrefsData() {
        onBoardingScreenPref = getApplicationContext().getSharedPreferences("onBoardingPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = onBoardingScreenPref.edit();
        editor.putBoolean("isoNboardOpened",true);
        editor.apply();
     }

    private static class ScreenAdaptor extends FragmentStatePagerAdapter{

        public ScreenAdaptor(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new onBoardingFragment1();
                case 1:
                    return new onBoardingFragment2();
                case 2:
                    return new onBoardingFragment3();
                case 3:
                    return new onBoardingFragment4();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}