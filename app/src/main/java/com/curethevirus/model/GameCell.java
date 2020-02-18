package com.curethevirus.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import com.curethevirus.GameActivity;

/**
 * This class displays individual cell information such as
 * whether or not it is a virus and its location.
 */

public class GameCell {

    private int rowLocation;
    private int columnLocation;
    private boolean isVirus;
    private boolean isFlipped;
    private boolean isVirusClicked;
    private Button button;

    public GameCell(int rowLocation, int columnLocation, boolean isVirus, Button button) {
        this.rowLocation = rowLocation;
        this.columnLocation = columnLocation;
        this.isVirus = isVirus;
        this.isFlipped = false;
        this.isVirusClicked = false;
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

    public boolean isVirusClicked() {
        return isVirusClicked;
    }

    public void setVirusClicked(boolean virusClicked) {
        isVirusClicked = virusClicked;
    }

    public boolean isVirus() {
        return isVirus;
    }

    public boolean isFlipped() {
        return isFlipped;
    }
}
