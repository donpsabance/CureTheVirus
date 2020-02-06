package com.curethevirus.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains a list of all game cells and the location
 * Functions: Find virus in row and columns
 *            Generate viruses
 *
 */

public class GameBoard {

    private int rows;
    private int columns;
    private int virusCount;

    private void scanViruses(int currentRow, int currentCol){

        int boardSize = rows * columns - 1;
        int currentLocation = currentRow * columns + currentCol;

        //search vertically from position
        for(int i = 0; i < boardSize; i++){


        }
    }

    private List<Integer> generateVirus(){

        Random random = new Random();
        List<Integer> generatedVirusLocations = new ArrayList<>();

        while(generatedVirusLocations.size() < virusCount){

            int generated = random.nextInt(rows * columns);

            if(!generatedVirusLocations.contains(generated)){
                generatedVirusLocations.add(generated);
            }
        }
        return generatedVirusLocations;
    }
}
