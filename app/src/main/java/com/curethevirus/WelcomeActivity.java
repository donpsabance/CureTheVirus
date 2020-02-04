package com.curethevirus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    private boolean skipped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, 200f, 200f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(7000);

        final ImageView virus1 = findViewById(R.id.virus1);
        virus1.startAnimation(rotateAnimation);

        final ImageView virus2 = findViewById(R.id.virus2);
        virus2.startAnimation(rotateAnimation);

        final ImageView virus3 = findViewById(R.id.virus3);
        virus3.startAnimation(rotateAnimation);

        final ImageView virus4 = findViewById(R.id.virus4);
        virus4.startAnimation(rotateAnimation);

        final ImageView virus5 = findViewById(R.id.virus5);
        virus5.startAnimation(rotateAnimation);

        Button skipBtn = findViewById(R.id.skip);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipped = true;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Thread myThread = new Thread() {
            @Override
            public void run() {
                    try {
                        sleep(100000);
                        if(skipped == false) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        };

        myThread.start();
    }
}
