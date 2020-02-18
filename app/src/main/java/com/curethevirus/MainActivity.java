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
import com.curethevirus.model.GameStatistics;

public class MainActivity extends AppCompatActivity {

    private static final int SETTINGS_REQUEST = 1;
    private static final int PLAY_REQUEST = 0;

    private GameSettings gameSettings;
    private GameStatistics gameStatistics;

    public static Intent makeIntent(Context context){
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //animation for rotating icon
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, 300f, 260f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(7000);

        final ImageView imageView = findViewById(R.id.mainIconView);
        imageView.startAnimation(rotateAnimation);

        gameSettings = GameSettings.getInstance();
        gameStatistics = GameStatistics.getInstance();

        //load buttons
        loadPlayButton();
        loadGameHelpButton();
        loadSettingsButton();

        //load game settings
        loadGameSettings();

        //load game statistics
        loadGameStatistics();

    }

    @Override
    protected void onStart(){

        super.onStart();
        //full screen immersive experience - taken from android docs
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void loadGameSettings() {

        final SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.settingSharedPref), Context.MODE_PRIVATE);

        gameSettings.setRows(sharedPreferences.getInt("rows", 0));
        gameSettings.setColumns(sharedPreferences.getInt("columns", 0));
        gameSettings.setVirusCount(sharedPreferences.getInt("virusCount", 0));

        //load defaults if its first time playing since there would be no SharedPreference file
        if(gameSettings.getRows() == 0 && gameSettings.getColumns() == 0 && gameSettings.getVirusCount() == 0){

               gameSettings.setRows(4);
               gameSettings.setColumns(6);
               gameSettings.setVirusCount(6);

        }
    }


    private void loadGameStatistics() {

        final SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.settingSharedPref), Context.MODE_PRIVATE);

        gameStatistics.setCurrentMoves(sharedPreferences.getInt("currentMoves", 0));
        gameStatistics.setCurrentVirusFound(sharedPreferences.getInt("currentVirusFound", 0));

        gameStatistics.setGamesPlayed(sharedPreferences.getInt("gamesPlayed", 0));
        gameStatistics.setBest4x6Game(sharedPreferences.getInt("best4x6", 0));
        gameStatistics.setBest5x10Game(sharedPreferences.getInt("best5x10", 0));
        gameStatistics.setBest6x15Game(sharedPreferences.getInt("best6x15", 0));

    }

    private void loadPlayButton(){

        Button button = findViewById(R.id.playButton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = GameActivity.makeIntent(MainActivity.this);
                startActivityForResult(intent, PLAY_REQUEST);

                gameStatistics.setGamesPlayed(gameStatistics.getGamesPlayed() + 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == PLAY_REQUEST){
            if(resultCode == RESULT_CANCELED){

                //reset game statistics, but not games played
                gameStatistics.setCurrentMoves(0);
                gameStatistics.setCurrentVirusFound(0);

            }
        }
    }
}
