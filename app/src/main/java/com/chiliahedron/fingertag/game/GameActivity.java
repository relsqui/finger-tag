package com.chiliahedron.fingertag.game;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.chiliahedron.fingertag.Interstitial;


public class GameActivity extends AppCompatActivity {
    public static final String TAG = GameActivity.class.getSimpleName();
    GameEngine game = new GameEngine();
    GameView gameView;

    @TargetApi(18)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Activity created. Restoring state from preferences, if there is any.");
        SharedPreferences sharedPrefs = getSharedPreferences("com.chiliahedron.fingertag", MODE_PRIVATE);
        game.setHighScore(sharedPrefs.getInt("com.chiliahedron.fingertag.HIGH_SCORE", 0));
        game.addScore(sharedPrefs.getInt("com.chiliahedron.fingertag.CURRENT_SCORE", 0));
        gameView = new GameView(this);
        gameView.setGame(game);
        setContentView(gameView);
        // Lock the screen orientation for the game (the HUD will manage its own rotation).
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    @Override
    protected void onPause() {
        // No parallel onResume because SurfaceView.onCreate is doing that work instead.
        super.onPause();
        gameView.stopThread();
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
        game.addScore(savedInstanceState.getInt("currentScore", 0));
    }

    public void onGameFinished() {
        Intent intent = new Intent(this, Interstitial.class);
        intent.putExtra("com.chiliahedron.fingertag.SCORE", game.getScore());
        // Can't do this until now because we need the score.
        game.clearState();
        // Save state after so we get the high score and not the current score.
        saveState();
        startActivity(intent);
        // Let the orientation change again.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        finish();
    }

    private void saveState() {
        SharedPreferences.Editor spEditor = getSharedPreferences("com.chiliahedron.fingertag", MODE_PRIVATE).edit();
        spEditor.putInt("com.chiliahedron.fingertag.HIGH_SCORE", game.getHighScore());
        spEditor.putInt("com.chiliahedron.fingertag.CURRENT_SCORE", game.getScore());
        spEditor.apply();
    }
}
