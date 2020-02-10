package com.curethevirus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameHelpActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context){
        Intent intent = new Intent(context, GameHelpActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_help);

        //full screen immersive experience - taken from android docs
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //load button
        loadBackButton();

        //make links clickable

//        TextView t1 = findViewById(R.id.cmpt276);
//        t1.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void loadBackButton(){

        Button button = findViewById(R.id.helpBackButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
