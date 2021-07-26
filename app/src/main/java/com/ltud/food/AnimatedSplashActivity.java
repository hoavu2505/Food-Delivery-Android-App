package com.ltud.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AnimatedSplashActivity extends AppCompatActivity {

    private ImageView tvTextLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animated_splash);

        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        tvTextLogo = findViewById(R.id.imv_text_logo);
        tvTextLogo.setAnimation(topAnim);

        SharedPreferences sharedPref = getSharedPreferences("locationPreference", CurrentLocationActivity.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!sharedPref.contains("location"))
                {
                    startActivity(new Intent(AnimatedSplashActivity.this, LocationPermissionActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
                else {
                    startActivity(new Intent(AnimatedSplashActivity.this, CurrentLocationActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
            }
        }, 2000);
    }
}