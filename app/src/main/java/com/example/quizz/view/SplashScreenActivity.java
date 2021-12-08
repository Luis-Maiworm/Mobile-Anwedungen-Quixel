package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.quizz.R;

/**
 *  App Splash Screen
 */
@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler splashScreenHandler = new Handler();
        splashScreenHandler.postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, MainMenuActivity.class));
            finish();
        }, 3000);

    }
}