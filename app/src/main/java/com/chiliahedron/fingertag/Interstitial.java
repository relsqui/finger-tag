package com.chiliahedron.fingertag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chiliahedron.fingertag.game.GameActivity;

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

    public void playAgain(View view) {
        startActivity(new Intent(this, GameActivity.class));
        finish();
    }
}
