package com.curethevirus.model;

public class GameStatistics {

    //does not need to be saved after app shutdown
    private int currentMoves;
    private int currentVirusFound;

    //store in Shared Preference
    private int gamesPlayed;
    private int best4x6Game;
    private int best5x10Game;
    private int best6x15Game;

    private static GameStatistics instance;

    public static GameStatistics getInstance(){
        if(instance == null){
            instance = new GameStatistics();
        }
        return instance;
    }


    private GameStatistics(){
        this.currentMoves = 0;
        this.currentVirusFound = 0;
        this.gamesPlayed = 0;
        this.best4x6Game = 0;
        this.best5x10Game = 0;
        this.best6x15Game = 0;
    }

    public void resetStatistics(){

        this.currentMoves = 0;
        this.currentVirusFound = 0;
        this.gamesPlayed = 0;
        this.best4x6Game = 0;
        this.best5x10Game = 0;
        this.best6x15Game = 0;

    }

    public int getCurrentMoves() {
        return currentMoves;
    }

    public void setCurrentMoves(int currentMoves) {
        this.currentMoves = currentMoves;
    }

    public int getCurrentVirusFound() {
        return currentVirusFound;
    }

    public void setCurrentVirusFound(int currentVirusFound) {
        this.currentVirusFound = currentVirusFound;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getBest4x6Game() {
        return best4x6Game;
    }

    public int getBest5x10Game() {
        return best5x10Game;
    }

    public int getBest6x15Game() {
        return best6x15Game;
    }

    public void setBest4x6Game(int best4x6Game) {
        this.best4x6Game = best4x6Game;
    }

    public void setBest5x10Game(int best5x10Game) {
        this.best5x10Game = best5x10Game;
    }

    public void setBest6x15Game(int best6x15Game) {
        this.best6x15Game = best6x15Game;
    }
}
