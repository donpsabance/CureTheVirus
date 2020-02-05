package com.curethevirus.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import com.curethevirus.GameActivity;

/**
    Individual cell information, whether it's a virus or not

 */

public class GameCell{

    private Button button;
    private Drawable backgroundImage;
    private boolean isVirus;

    public GameCell(Button button, boolean virus, Drawable backgroundImage){
        this.button = button;
        this.isVirus = virus;
        this.backgroundImage = backgroundImage;
    }

    public boolean isVirus(){
        return this.isVirus;
    }

    private void setButtonBackground(){
        button.setBackground(backgroundImage);
    }
}
