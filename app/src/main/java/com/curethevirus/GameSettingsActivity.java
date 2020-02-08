package com.curethevirus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.curethevirus.model.GameSettings;
import com.curethevirus.model.GameStatistics;

public class GameSettingsActivity extends AppCompatActivity {

    private GameSettings gameSettings;
    private GameStatistics gameStatistics;

    public static Intent makeIntent(Context context){
        return new Intent(context, GameSettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        //full screen immersive experience - taken from android docs
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        gameSettings = GameSettings.getInstance();
        gameStatistics = GameStatistics.getInstance();

        loadStatistics();

        //load radio buttons
        loadBoardSizeRadioButtons();
        loadVirusCountRadioButtons();

        loadBackButton();
        loadResetStatisticsButton();

    }

    @Override
    protected void onStart(){

        super.onStart();
        loadGameSettings();
    }

    @Override
    protected void onPause(){

        super.onPause();
        saveGameSettings();

    }

    private void saveGameSettings() {

        final SharedPreferences sharedPreferences = GameSettingsActivity.this.getSharedPreferences(getString(R.string.settingSharedPref), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("rows", gameSettings.getRows()).apply();
        editor.putInt("columns", gameSettings.getColumns()).apply();
        editor.putInt("virusCount", gameSettings.getVirusCount()).apply();

    }

    private void loadStatistics() {

        TextView textView4x6 = findViewById(R.id.highScore4x6);
        TextView textView5x10 = findViewById(R.id.highScore5x10);
        TextView textView6x15 = findViewById(R.id.highScore6x15);
        TextView textViewGamesPlayed = findViewById(R.id.gamesPlayedTextView);

        textView4x6.setText(getResources().getText(R.string._4x6).toString() + " " + gameStatistics.getBest4x6Game() + " moves");
        textView5x10.setText(getResources().getText(R.string._5x10).toString() + " " + gameStatistics.getBest5x10Game() + " moves");
        textView6x15.setText(getResources().getText(R.string._6x15).toString() + " " + gameStatistics.getBest6x15Game() + " moves");
        textViewGamesPlayed.setText(getResources().getText(R.string.times_played).toString() + " " + gameStatistics.getGamesPlayed());

    }

    private void loadGameSettings() {

        final SharedPreferences sharedPreferences = GameSettingsActivity.this.getSharedPreferences(getString(R.string.settingSharedPref), Context.MODE_PRIVATE);

        RadioGroup boardSizeRadioGroup = findViewById(R.id.boardSizeRadioGroup);
        RadioGroup virusRadioGroup = findViewById(R.id.virusCountRadioGroup);
        String[] boardSizeValues = getResources().getStringArray(R.array.boardSize);
        int[] virusCountValues = getResources().getIntArray(R.array.virusAmount);

        for(int i = 0; i < boardSizeValues.length; i++){

            int rows = gameSettings.getRows();
            int columns = gameSettings.getColumns();

            if(Integer.parseInt(boardSizeValues[i].split("x")[0]) == rows &&
                Integer.parseInt(boardSizeValues[i].split("x")[1]) == columns){

                RadioButton radioButton = (RadioButton) boardSizeRadioGroup.getChildAt(i);
                radioButton.setChecked(true);

            }
        }

        for(int i = 0; i < virusCountValues.length; i++){

            int virusCount = gameSettings.getVirusCount();

            if(virusCountValues[i] == virusCount){

                RadioButton radioButton = (RadioButton) virusRadioGroup.getChildAt(i);
                radioButton.setChecked(true);

            }
        }
    }

    private void loadBoardSizeRadioButtons() {

        final SharedPreferences sharedPreferences = GameSettingsActivity.this.getSharedPreferences(getString(R.string.settingSharedPref), Context.MODE_PRIVATE);
        RadioGroup radioGroup = findViewById(R.id.boardSizeRadioGroup);

        String[] values = getResources().getStringArray(R.array.boardSize);

        for(int i = 0; i < values.length; i++){

            final String currentValue = values[i];

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(values[i]);
            radioButton.setTypeface(Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL));
            radioButton.setTextSize(18);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //load the values, split at 'x' because they are formatted to be row x cols
                    gameSettings.setRows(Integer.parseInt(currentValue.split("x")[0]));
                    gameSettings.setColumns(Integer.parseInt(currentValue.split("x")[1]));

                }
            });

            radioGroup.addView(radioButton);
        }

        int rows = sharedPreferences.getInt("rows", 0);
        int columns = sharedPreferences.getInt("columns", 0);

        if(rows == 0 && columns == 0){

            //set defaults
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(0);
            radioButton.setChecked(true);

            gameSettings.setRows(Integer.parseInt (((RadioButton) radioGroup.getChildAt(0)).getText().toString().split("x")[0] + ""));
            gameSettings.setColumns(Integer.parseInt (((RadioButton) radioGroup.getChildAt(0)).getText().toString().split("x")[1] + ""));

        }
    }

    private void loadVirusCountRadioButtons() {

        final SharedPreferences sharedPreferences = GameSettingsActivity.this.getSharedPreferences(getString(R.string.settingSharedPref), Context.MODE_PRIVATE);
        RadioGroup radioGroup = findViewById(R.id.virusCountRadioGroup);

        int[] values = getResources().getIntArray(R.array.virusAmount);

        for(int i = 0; i < values.length; i++){

            final int currentValue = values[i];

            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(values[i] + " viruses");
            radioButton.setTypeface(Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL));
            radioButton.setTextSize(18);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    gameSettings.setVirusCount(currentValue);

                }
            });

            radioGroup.addView(radioButton);

        }

        int virusCount = sharedPreferences.getInt("virusCount", 0);
        if(virusCount == 0){

            //set defaults
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(0);
            radioButton.setChecked(true);
            gameSettings.setVirusCount(Integer.parseInt (((RadioButton) radioGroup.getChildAt(0)).getText().toString().split(" ")[0] + ""));

        }
    }

    private void loadBackButton(){

        Button button = findViewById(R.id.settingsBackButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void loadResetStatisticsButton(){

        Button button = findViewById(R.id.settingsResetButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameStatistics.resetStatistics();
                loadStatistics();

                Toast.makeText(GameSettingsActivity.this, "Statistics have been reset!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
