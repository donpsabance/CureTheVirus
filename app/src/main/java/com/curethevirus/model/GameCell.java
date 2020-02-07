package com.curethevirus.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import com.curethevirus.GameActivity;

/**
    Individual cell information, whether it's a virus or not

 */

public class GameCell{

    private int rowLocation;
    private int columnLocation;
    private boolean isVirus;
    private boolean isFlipped;
    private Button button;

    public GameCell(int rowLocation, int columnLocation, boolean isVirus, Button button) {
        this.rowLocation = rowLocation;
        this.columnLocation = columnLocation;
        this.isVirus = isVirus;
        this.isFlipped = false;
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    public void setVirus(boolean virus) {
        isVirus = virus;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public boolean isVirus() {
        return isVirus;
    }

    public boolean isFlipped() {
        return isFlipped;
    }
}
