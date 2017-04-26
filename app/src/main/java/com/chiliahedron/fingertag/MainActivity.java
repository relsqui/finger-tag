package com.chiliahedron.fingertag;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    MainView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = new MainView(this);
        setContentView(mainView);
        GameEngine game = mainView.getGame();
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        game.setHighScore(sharedPrefs.getInt("com.chiliahedron.fingertag.HIGH_SCORE", 0));
        game.setScore(sharedPrefs.getInt("com.chiliahedron.fingertag.CURRENT_SCORE", 0));
        Log.d(TAG, "Restoring state from preferences, if there is any.");
    }

    @Override
    public void onStop() {
        super.onStop();
        mainView.stopThread();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        GameEngine game = mainView.getGame();
        savedInstanceState.putInt("highScore", game.getScore());
        savedInstanceState.putInt("currentScore", game.getHighScore());
        SharedPreferences.Editor spEditor = getPreferences(MODE_PRIVATE).edit();
        spEditor.putInt("com.chiliahedron.fingertag.HIGH_SCORE", game.getHighScore());
        spEditor.putInt("com.chiliahedron.fingertag.CURRENT_SCORE", game.getScore());
        spEditor.apply();
        Log.d(TAG, "Saving instance state.");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        GameEngine game = mainView.getGame();
        Log.d(TAG, "Restoring state from bundle.");
        game.setHighScore(savedInstanceState.getInt("highScore", 0));
        int currentScore = savedInstanceState.getInt("currentScore", 0);
        game.setScore(currentScore);
    }
}
