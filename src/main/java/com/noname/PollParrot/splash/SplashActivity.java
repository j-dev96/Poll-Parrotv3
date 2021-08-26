package com.noname.PollParrot.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.noname.PollParrot.R;
import com.noname.PollParrot.homedashboard.activity.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler(Looper.getMainLooper())
                .postDelayed(() -> {
                            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                            finish();
                        },
                        5000);
    }
}