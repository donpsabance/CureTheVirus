package com.curethevirus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.curethevirus.model.GameCell;
import com.curethevirus.model.GameCellManager;
import com.curethevirus.model.GameSettings;
import com.curethevirus.model.GameStatistics;


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

        //get rows and columns from settings
        gameSettings = GameSettings.getInstance();
        gameStatistics = GameStatistics.getInstance();
        getBoardSize();

        gameCellManager = new GameCellManager(rows, columns, virusCount);

        //load game play elements
        loadCells();
        gameCellManager.generateVirus();
        updateGameStatistics();

    }

    @Override
    protected void onStart(){

        super.onStart();
        //full screen immersive experience - taken from android docs
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onPause(){

        super.onPause();

        //save statistics
        final SharedPreferences sharedPreferences = GameActivity.this.getSharedPreferences(getString(R.string.settingSharedPref), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("gamesPlayed", gameStatistics.getGamesPlayed()).apply();
        editor.putInt("best4x6", gameStatistics.getBest4x6Game());
        editor.putInt("best5x10", gameStatistics.getBest5x10Game());
        editor.putInt("best6x15", gameStatistics.getBest6x15Game());
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

                        if(gameStatistics.getCurrentVirusFound() == gameSettings.getVirusCount()) {
                            endDialog();
                            updateHighscore();

                            gameStatistics.setCurrentVirusFound(0);
                            gameStatistics.setCurrentMoves(0);
                        }
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
                gameStatistics.setCurrentVirusFound(gameStatistics.getCurrentVirusFound() + 1);

                final MediaPlayer player = MediaPlayer.create(this, R.raw.success);
                player.start();

            } else if((!gameCell.isVirusClicked()) && gameCell.isFlipped()){

                //update virus cells
                gameCell.setVirusClicked(true);
                updateButtons(row, col);
                gameStatistics.setCurrentMoves(gameStatistics.getCurrentMoves() + 1);

            }
        } else {

            GameCell gameCell = gameCellManager.getGameCells().get(row).get(col);

            if(!gameCell.isFlipped()){

                final MediaPlayer player = MediaPlayer.create(this, R.raw.scan);
                player.start();

                Button button = gameCell.getButton();
                String text = findViruses(row, col, true) + "";

                gameCell.setFlipped(true);
                button.setText(text);

                updateButtons(row, col);
                gameStatistics.setCurrentMoves(gameStatistics.getCurrentMoves() + 1);

            }
        }
    }

    private int findViruses(int row, int col, boolean animate){

        GameCell gameCell;
        int found = 0;

        //check all vertical ones
        for(int i = 0; i < rows; i++){

            gameCell = gameCellManager.getGameCells().get(i).get(col);

            if(!gameCell.isFlipped() && animate){

                //https://stackoverflow.com/questions/15006369/bounce-button-on-tap
                ObjectAnimator animator = ObjectAnimator.ofFloat(gameCell.getButton(), "translationY" , - 50f, 0f);
                animator.setDuration(1200);
                animator.setInterpolator(new BounceInterpolator());
                animator.start();
            }

            if(gameCell.isVirus() && !gameCell.isFlipped()){
                found++;
            }
        }

        //check all horizontal ones
        for(int i = 0; i < columns; i++) {

            gameCell = gameCellManager.getGameCells().get(row).get(i);

            if(!gameCell.isFlipped() && animate){

                //https://stackoverflow.com/questions/15006369/bounce-button-on-tap
                ObjectAnimator animator = ObjectAnimator.ofFloat(gameCell.getButton(), "translationY" , - 50f, 0f);
                animator.setDuration(1200);
                animator.setInterpolator(new BounceInterpolator());
                animator.start();
            }

            if (gameCell.isVirus() && !gameCell.isFlipped()){
                found++;
            }
        }
        return found;
    }

    //update flipped buttons with the correct remaining amount of virus left
    private void updateButtons(int row, int col){

        GameCell gameCell;

        //check all vertical ones
        for(int i = 0; i < rows; i++){

            gameCell = gameCellManager.getGameCells().get(i).get(col);
            Button button = gameCell.getButton();

            if(gameCell.isFlipped() && !gameCell.isVirus() || (gameCell.isVirusClicked())){
                String text = findViruses(i, col, false) + "";
                button.setText(text);
            }

            if(!gameCell.isFlipped()){

                //https://stackoverflow.com/questions/15006369/bounce-button-on-tap
                ObjectAnimator animator = ObjectAnimator.ofFloat(gameCell.getButton(), "translationY" , - 50f, 0f);
                animator.setDuration(1200);
                animator.setInterpolator(new BounceInterpolator());
                animator.start();
            }
        }

        //check all horizontal ones
        for(int i = 0; i < columns; i++){

            gameCell = gameCellManager.getGameCells().get(row).get(i);
            Button button = gameCell.getButton();

            if(gameCell.isFlipped() && !gameCell.isVirus() || (gameCell.isVirusClicked())){
                String text = findViruses(row, i, false) + "";
                button.setText(text);
            }

            if(!gameCell.isFlipped()){

                //https://stackoverflow.com/questions/15006369/bounce-button-on-tap
                ObjectAnimator animator = ObjectAnimator.ofFloat(gameCell.getButton(), "translationY" , - 50f, 0f);
                animator.setDuration(1200);
                animator.setInterpolator(new BounceInterpolator());
                animator.start();
            }
        }
    }

    private void updateGameStatistics() {

        TextView minesFound = findViewById(R.id.minesFoundTextView);
        TextView currentMoves = findViewById(R.id.currentMovesTextView);
        TextView gamesPlayed = findViewById(R.id.gamesPlayedTextView);

        String mines = getResources().getText(R.string.mines_found).toString() + " " + gameStatistics.getCurrentVirusFound() + " of " + gameSettings.getVirusCount();
        String moves = getResources().getText(R.string.of_scans_used).toString() + " " + gameStatistics.getCurrentMoves();
        String played = getResources().getText(R.string.times_played).toString() + " " + gameStatistics.getGamesPlayed();

        minesFound.setText(mines);
        currentMoves.setText(moves);
        gamesPlayed.setText(played);

    }

    private void endDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View myView = getLayoutInflater().inflate(R.layout.activity_end_dialog, null);
        Button btn = myView.findViewById(R.id.ok);
        alert.setCancelable(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alert.setView(myView);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void updateHighscore(){

        if(rows == 4 && columns == 6){
            if(gameStatistics.getCurrentMoves() < gameStatistics.getBest4x6Game() && gameStatistics.getBest4x6Game() != 0){
                gameStatistics.setBest4x6Game(gameStatistics.getCurrentMoves());
            } else if(gameStatistics.getBest4x6Game() == 0) {
                gameStatistics.setBest4x6Game(gameStatistics.getCurrentMoves());
            }
        } else if(rows == 5 && columns == 10){
            if(gameStatistics.getCurrentMoves() < gameStatistics.getBest5x10Game()) {
                gameStatistics.setBest5x10Game(gameStatistics.getCurrentMoves());
            }else if(gameStatistics.getBest5x10Game() == 0) {
                gameStatistics.setBest5x10Game(gameStatistics.getCurrentMoves());
            }
        } else if(rows == 6 && columns == 15){
            if(gameStatistics.getCurrentMoves() < gameStatistics.getBest6x15Game()) {
                gameStatistics.setBest6x15Game(gameStatistics.getCurrentMoves());
            } else if(gameStatistics.getBest6x15Game() == 0) {
                gameStatistics.setBest6x15Game(gameStatistics.getCurrentMoves());
            }
        }
    }
}
