package com.example.upiapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.upiapp.utils.LocalDataStore;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LocalDataStore dataStore = new LocalDataStore(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // 🔐 If user already logged in → MainActivity
            if (dataStore.getSavedUsername() != null) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // 🔑 Otherwise → Login
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

            finish();

        }, SPLASH_TIME);
    }
}
