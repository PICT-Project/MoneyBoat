package com.example.tahakothawala.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import static java.lang.Thread.sleep;

public class WelcomePage extends AppCompatActivity {
    SharedPreferences sharedPreferences,sharedPreferences2;
    public static String phone = "#";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences("info",MODE_PRIVATE);
                Long ml = sharedPreferences.getLong("phone",0);
                if(!(ml.equals(0L))){
                    Intent myIntent = new Intent(WelcomePage.this, Home2.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myIntent);
                }
                else {
                    Intent myIntent = new Intent(WelcomePage.this, MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myIntent);
                }

            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 2000);
    }
}

