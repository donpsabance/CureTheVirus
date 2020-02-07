package com.curethevirus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.curethevirus.model.GameCell;
import com.curethevirus.model.GameCellManager;
import com.curethevirus.model.GameSettings;
import com.curethevirus.model.GameStatistics;

import org.w3c.dom.Text;

public class GameActivity extends AppCompatActivity {

    private GameSettings gameSettings;
    private GameStatistics gameStatistics;
    private GameCellManager gameCellManager;

    private int rows;
    private int columns;
    private int virusCount;

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
        gameSettings = GameSettings.getInstance();
        gameStatistics = new GameStatistics();
        getBoardSize();

        gameCellManager = new GameCellManager(rows, columns, virusCount);

        //load game play elements
        loadCells();
        gameCellManager.generateVirus();

    }

    private void getBoardSize() {

        rows = gameSettings.getRows();
        columns = gameSettings.getColumns();
        virusCount = gameSettings.getVirusCount();

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

                final int currentRow = i;
                final int currentCol = j;

                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //scan the cells
                        lockButtonSize();
                        scanCell(currentRow, currentCol);
                        updateGameStatistics();

                    }
                });

                GameCell gameCell = new GameCell(i, j, false, button);
                gameCellManager.addCell(gameCell, i, j);
                tableRow.addView(button);
            }
        }
    }

    private void lockButtonSize(){

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){

                Button button = gameCellManager.getGameCells().get(i).get(j).getButton();

                int width = button.getWidth();
                int height = button.getHeight();

                button.setMinWidth(width);
                button.setMaxWidth(width);

                button.setMinHeight(height);
                button.setMaxHeight(height);

            }
        }
    }

    public void scanCell(int row, int col){

        if(gameCellManager.getGameCells().get(row).get(col).isVirus()){

            GameCell gameCell = gameCellManager.getGameCells().get(row).get(col);

            if(!gameCell.isFlipped()){

                Button button = gameCell.getButton();

                int newWidth = gameCellManager.getGameCells().get(row).get(col).getButton().getWidth();
                int newHeight = gameCellManager.getGameCells().get(row).get(col).getButton().getHeight();

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.virus_icon);
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                button.setBackground(new BitmapDrawable(getResources(), scaled));

                gameCell.setFlipped(true);
                updateButtons(row, col);

                gameStatistics.setCurrentMoves(gameStatistics.getCurrentMoves() + 1);

            } else {

                //update virus cells

            }
        } else {

            GameCell gameCell = gameCellManager.getGameCells().get(row).get(col);

            if(!gameCell.isFlipped()){

                Button button = gameCell.getButton();

                int newWidth = gameCellManager.getGameCells().get(row).get(col).getButton().getWidth();
                int newHeight = gameCellManager.getGameCells().get(row).get(col).getButton().getHeight();


                gameCell.setFlipped(true);
                button.setText(findViruses(row, col) + "");

                updateButtons(row, col);

                gameStatistics.setCurrentMoves(gameStatistics.getCurrentMoves() + 1);

            }
        }
    }

    private int findViruses(int row, int col){

        GameCell gameCell;
        int found = 0;

        //check all vertical ones
        for(int i = 0; i < rows; i++){

            gameCell = gameCellManager.getGameCells().get(i).get(col);

            if(gameCell.isVirus() && !gameCell.isFlipped()){
                found++;
            }
        }

        //check all horizontal ones
        for(int i = 0; i < columns; i++) {

            gameCell = gameCellManager.getGameCells().get(row).get(i);

            if (gameCell.isVirus() && !gameCell.isFlipped()){
                found++;
            }
        }
        return found;
    }

    private void updateButtons(int row, int col){

        GameCell gameCell;

        //check all vertical ones
        for(int i = 0; i < rows; i++){

            gameCell = gameCellManager.getGameCells().get(i).get(col);
            Button button = gameCell.getButton();

            if(gameCell.isFlipped() && !gameCell.isVirus()){
                button.setText(findViruses(i, col) + "");

            }
        }

        //check all horizontal ones
        for(int i = 0; i < columns; i++){

            gameCell = gameCellManager.getGameCells().get(row).get(i);
            Button button = gameCell.getButton();

            if(gameCell.isFlipped() && !gameCell.isVirus()){
                button.setText(findViruses(row, i) + "");
            }
        }
    }

    private void updateGameStatistics() {

        TextView minesFound = findViewById(R.id.minesFoundTextView);
        TextView currentMoves = findViewById(R.id.currentMovesTextView);
        TextView gamesPlayed = findViewById(R.id.gamesPlayedTextView);

        minesFound.setText(getResources().getText(R.string.mines_found).toString() + " " + gameStatistics.getCurrentVirusFound() + " of " +gameSettings.getVirusCount());
        currentMoves.setText(getResources().getText(R.string.of_scans_used).toString() + " " + gameStatistics.getCurrentMoves());
        gamesPlayed.setText(getResources().getText(R.string.times_played).toString() + " " + gameStatistics.getGamesPlayed());

    }
}
