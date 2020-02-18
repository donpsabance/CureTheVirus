package com.curethevirus.model;

/**
 * This class sets up the number of columns, rows, and viruses in a new game.
 */


public class GameSettings {

    private int rows;
    private int columns;
    private int virusCount;

    private static GameSettings instance;

    private GameSettings() {

        rows = 0;
        columns = 0;
        virusCount = 0;
    }

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setVirusCount(int virusCount) {
        this.virusCount = virusCount;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getVirusCount() {
        return virusCount;
    }
}
