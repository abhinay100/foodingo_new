package com.foodingo.activities.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.foodingo.activities.R;

/**
 * Created by admin on 14-01-2016.
 */
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //check if user is already logged in after 2 sec
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent loginIntent = new Intent(Splash.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }, 2000);
    }
}