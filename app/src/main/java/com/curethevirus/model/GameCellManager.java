package com.curethevirus.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manage all the cells and stores location and information such as being a virus or not
 * Functions:
 *              searchVirus
 *              createVirus
 *
 */

public class GameCellManager {

    private int rows;
    private int columns;
    private int virusCount;

    private List<List<GameCell>> gameCells;

    public GameCellManager(int row, int col, int virusCount){
        gameCells = new ArrayList<>(rows);
        this.rows = row;
        this.columns = col;
        this.virusCount = virusCount;

        //instantiate columns
        for(int i = 0; i < rows; i++){
            gameCells.add(new ArrayList<GameCell>());
        }
    }

    public List<List<GameCell>> getGameCells(){
        return gameCells;
    }

    public void addCell(GameCell gameCell, int row, int col){

        gameCells.get(row).add(col, gameCell);

    }

    public void generateVirus(){

        Random random = new Random();
        List<Integer> virusLocations = new ArrayList<>();

        while(virusLocations.size() < virusCount){

            int generated = random.nextInt(rows * columns);

            if(!virusLocations.contains(generated)){
                virusLocations.add(generated);
            }
        }


        //now apply all virus to the cells
        //convert all locations into 2d location
        for(int i = 0; i < virusLocations.size(); i++){

            int location = virusLocations.get(i);
            int row = location / columns;
            int col = location % columns;

            gameCells.get(row).get(col).setVirus(true);

        }
    }
}
