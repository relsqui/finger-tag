/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chiliahedron.fingertag.game.GameActivity;

/** Activity which displays the interstitial (between game levels) screen. */
public class Interstitial extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);
        Intent intent = getIntent();
        TextView scoreDisplay = (TextView) findViewById(R.id.interScoreDisplay);
        scoreDisplay.setText(getString(R.string.your_score,
                intent.getIntExtra("com.chiliahedron.fingertag.SCORE", 0)));
    }

    /**
     * Responds to a Play Again button press by returning to the game activity.
     *
     * @param view The current {@link View}, passed automatically with the button press.
     */
    public void playAgain(View view) {
        startActivity(new Intent(this, GameActivity.class));
        // Finish this activity so the back button will go all the way back to the splash screen.
        finish();
    }
}
