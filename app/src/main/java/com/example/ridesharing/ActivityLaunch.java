package com.example.ridesharing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Активность холодного старта приложения
 */
public class ActivityLaunch extends AppCompatActivity {

    private static final String USER_SETTINGS = "RS_US";
    SharedPreferences userPreferences;
    boolean checkRememberUserSettings;
    Intent next;

    int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        userPreferences = getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        checkRememberUserSettings = userPreferences.getBoolean("checkRememberUserSettings", false);

        Timer myTimer;
        myTimer = new Timer();

        myTimer.schedule(new TimerTask() {
            public void run() {
                if (time <=5)
                    timerTick();
            }
        }, 0, 500); // каждые 0.5 секунды

        ImageView im = findViewById(R.id.imageView2);
        Glide.with(this)
                .load(R.drawable.full_ridesharing_launch_logo)
                //.apply(RequestOptions.bitmapTransform(new BlurTransformation(25,3)))
                .into(im);
    }

    private void timerTick() {
        this.runOnUiThread(doTask);
    }

    private Runnable doTask = new Runnable() {
        public void run() {
            switch (time){
                case 2:
                    if (!checkRememberUserSettings){
                        next = new Intent(ActivityLaunch.this, ActivityLogin.class);
                    }else{
                        next = new Intent(ActivityLaunch.this, ActivityLogin.class);
                    }
                    startActivity(next);
                    finish();
                    break;
            }
            time++;
        }
    };
}