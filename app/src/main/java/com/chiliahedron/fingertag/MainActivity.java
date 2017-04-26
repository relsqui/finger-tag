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
        Log.d(TAG, "Restoring state from preferences, if there is any.");
        GameEngine game = mainView.getGame();
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        game.setHighScore(sharedPrefs.getInt("com.chiliahedron.fingertag", 0));
        Log.d(TAG, "Creating a new activity.");
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
        savedInstanceState.putInt("currentScore", game.getScore());
        savedInstanceState.putInt("highScore", game.getHighScore());
        SharedPreferences.Editor spEditor = getPreferences(MODE_PRIVATE).edit();
        spEditor.putInt("com.chiliahedron.fingertag", game.getHighScore());
        spEditor.apply();
        Log.d(TAG, "Saving instance state. " + savedInstanceState);
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
