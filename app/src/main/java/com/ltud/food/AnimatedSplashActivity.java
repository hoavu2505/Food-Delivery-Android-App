package com.ltud.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class AnimatedSplashActivity extends AppCompatActivity {

    private TextView tvTextLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animated_splash);

        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        tvTextLogo = findViewById(R.id.tv_text_logo);
        tvTextLogo.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(AnimatedSplashActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        }, 1800);
    }
}