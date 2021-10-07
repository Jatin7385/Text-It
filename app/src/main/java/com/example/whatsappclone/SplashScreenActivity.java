package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    private int loginStatus = 0;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {loginStatus = 1;}


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loginStatus == 1){intent = new Intent(SplashScreenActivity.this,MainActivity.class); }
                else {
                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                }
                finish();
                startActivity(intent);
            }
        }, 3000);
    }
}