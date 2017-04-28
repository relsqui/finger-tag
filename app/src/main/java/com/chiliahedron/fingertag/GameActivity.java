package com.chiliahedron.fingertag;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class GameActivity extends AppCompatActivity {
    public static final String TAG = GameActivity.class.getSimpleName();
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Activity created. Restoring state from preferences, if there is any.");
        gameView = new GameView(this);
        setContentView(gameView);
        GameEngine game = gameView.getGame();
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        game.setHighScore(sharedPrefs.getInt("com.chiliahedron.fingertag.HIGH_SCORE", 0));
        game.setScore(sharedPrefs.getInt("com.chiliahedron.fingertag.CURRENT_SCORE", 0));
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.stopThread();
        // No parallel onResume because SurfaceView.onCreate is doing that work instead.
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "Saving state from activity.");
        GameEngine game = gameView.getGame();
        savedInstanceState.putInt("highScore", game.getScore());
        savedInstanceState.putInt("currentScore", game.getHighScore());
        SharedPreferences.Editor spEditor = getPreferences(MODE_PRIVATE).edit();
        spEditor.putInt("com.chiliahedron.fingertag.HIGH_SCORE", game.getHighScore());
        spEditor.putInt("com.chiliahedron.fingertag.CURRENT_SCORE", game.getScore());
        spEditor.apply();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "Restoring state from bundle.");
        GameEngine game = gameView.getGame();
        game.setHighScore(savedInstanceState.getInt("highScore", 0));
        game.setScore(savedInstanceState.getInt("currentScore", 0));
    }
}
