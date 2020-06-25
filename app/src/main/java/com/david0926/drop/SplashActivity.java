package com.david0926.drop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.david0926.drop.util.SharedPreferenceUtil;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //fullscreen

        new Handler().postDelayed(() -> {
            boolean isLandingShown = SharedPreferenceUtil.getBoolean(this, "landing_shown", false);
            //isLandingShown = false; //remove this line, to show landing page only once

            if (isLandingShown) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashActivity.this, LandingActivity.class);
                startActivity(intent);
            }
            finish();

        }, 2000);
    }
}
