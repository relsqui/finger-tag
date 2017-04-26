package com.chiliahedron.fingertag;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    MainView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = new MainView(this);
        setContentView(mainView);
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }

    /*
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor spEditor = getPreferences(MODE_PRIVATE).edit();
        spEditor.putInt("Finger Tag high score", mainView.getGame().getHighScore());
        spEditor.commit();
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        mainView.getGame().setHighScore(sharedPrefs.getInt("Finger Tag high score", 0));
    }
    */
}
