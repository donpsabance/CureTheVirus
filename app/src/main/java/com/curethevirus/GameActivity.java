package com.curethevirus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Map;

public class GameActivity extends AppCompatActivity {

    private int rows;
    private int columns;

    public static Intent makeIntent(Context context){
        return new Intent(context, GameActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        //full screen immersive experience - taken from android docs
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get rows and columns from settings
        getBoardSize();

        //load button cells
        loadCells();

    }

    private void getBoardSize() {

        final SharedPreferences sharedPreferences = GameActivity.this.getSharedPreferences(getString(R.string.settingSharedPref), Context.MODE_PRIVATE);

        rows = sharedPreferences.getInt("rows", 0);
        columns = sharedPreferences.getInt("columns", 0);

    }


    private void loadCells(){

        TableLayout tableLayout = findViewById(R.id.cellsTable);

        for(int i = 0; i < rows; i++){

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));

            tableLayout.addView(tableRow);

            for(int j = 0; j < columns; j++){

                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //scan the cells
                    }
                });

                tableRow.addView(button);
            }
        }
    }
}
