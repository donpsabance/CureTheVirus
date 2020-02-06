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
    private Button button;

    public GameCell(int rowLocation, int columnLocation, boolean isVirus, Button button) {
        this.rowLocation = rowLocation;
        this.columnLocation = columnLocation;
        this.isVirus = isVirus;
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    public boolean isVirus() {
        return isVirus;
    }
}
