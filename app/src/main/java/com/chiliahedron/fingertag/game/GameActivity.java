package com.chiliahedron.fingertag.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.chiliahedron.fingertag.Interstitial;


public class GameActivity extends AppCompatActivity {
    public static final String TAG = GameActivity.class.getSimpleName();
    GameEngine game = new GameEngine();
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Activity created. Restoring state from preferences, if there is any.");
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        game.setHighScore(sharedPrefs.getInt("com.chiliahedron.fingertag.HIGH_SCORE", 0));
        game.setScore(sharedPrefs.getInt("com.chiliahedron.fingertag.CURRENT_SCORE", 0));
        gameView = new GameView(this);
        gameView.setGame(game);
        setContentView(gameView);
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
        Log.d(TAG, "Saving state from interrupted activity.");
        savedInstanceState.putInt("highScore", game.getScore());
        savedInstanceState.putInt("currentScore", game.getHighScore());
        saveState();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "Restoring state from bundle.");
        game.setHighScore(savedInstanceState.getInt("highScore", 0));
        game.setScore(savedInstanceState.getInt("currentScore", 0));
    }

    public void onGameFinished() {
        Intent intent = new Intent(this, Interstitial.class);
        intent.putExtra("com.chiliahedron.fingertag.SCORE", game.getScore());
        // Can't do this until now because we need the score.
        game.clearState();
        // Save state after so we get the high score and not the current score.
        saveState();
        startActivity(intent);
        finish();
    }

    private void saveState() {
        SharedPreferences.Editor spEditor = getPreferences(MODE_PRIVATE).edit();
        spEditor.putInt("com.chiliahedron.fingertag.HIGH_SCORE", game.getHighScore());
        spEditor.putInt("com.chiliahedron.fingertag.CURRENT_SCORE", game.getScore());
        spEditor.apply();
    }
}
