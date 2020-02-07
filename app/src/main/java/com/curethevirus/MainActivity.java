package com.curethevirus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.curethevirus.model.GameCellManager;
import com.curethevirus.model.GameSettings;

public class MainActivity extends AppCompatActivity {

    private static final int SETTINGS_REQUEST = 1;
    private GameSettings gameSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //full screen immersive experience - taken from android docs
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //animation for rotating icon
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, 300f, 260f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(7000);

        final ImageView imageView = findViewById(R.id.mainIconView);
        imageView.startAnimation(rotateAnimation);

        gameSettings = gameSettings.getInstance();

        //load buttons
        loadPlayButton();
        loadGameHelpButton();
        loadSettingsButton();

        //load game settings
        loadGameSettings();

    }

    private void loadGameSettings() {

        final SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.settingSharedPref), Context.MODE_PRIVATE);

        gameSettings.setRows(sharedPreferences.getInt("rows", 0));
        gameSettings.setColumns(sharedPreferences.getInt("columns", 0));
        gameSettings.setVirusCount(sharedPreferences.getInt("virusCount", 0));

    }

    private void loadPlayButton(){

        Button button = findViewById(R.id.playButton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = GameActivity.makeIntent(MainActivity.this);
                startActivity(intent);

            }
        });
    }

    private void loadGameHelpButton(){

        Button button = findViewById(R.id.helpButton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = GameHelpActivity.makeIntent(MainActivity.this);
                startActivity(intent);

            }
        });
    }

    private void loadSettingsButton(){

        Button button = findViewById(R.id.settingsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GameSettingsActivity.makeIntent(MainActivity.this);
                startActivityForResult(intent, SETTINGS_REQUEST);
            }
        });
    }
}
