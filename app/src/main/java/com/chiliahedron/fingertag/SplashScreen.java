/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chiliahedron.fingertag.game.GameActivity;

/** Activity which displays the splash screen. */
public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView scoreView = (TextView) findViewById(R.id.splashScoreView);
        SharedPreferences sharedPrefs = getSharedPreferences("com.chiliahedron.fingertag",
                                                             MODE_PRIVATE);
        scoreView.setText(getString(R.string.high_score,
                sharedPrefs.getInt("com.chiliahedron.fingertag.HIGH_SCORE", 0)));
    }

    /**
     * Responds to the Start button by switching to the game activity.
     *
     * @param view The current {@link View}, passed automatically with the button press.
     */
    public void startGame(View view) {
        startActivity(new Intent(this, GameActivity.class));
    }
}
