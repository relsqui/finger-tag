package com.chiliahedron.fingertag.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.chiliahedron.fingertag.GameEngine;

public class HUD implements Renderer {
    private GameEngine game;
    private Paint scorePaint = new Paint();
    private Paint highScorePaint = new Paint();

    public HUD(GameEngine game) {
        this.game = game;
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(40);
        highScorePaint.setColor(Color.YELLOW);
        highScorePaint.setTextSize(40);
    }

    public void render(Canvas canvas) {
        canvas.drawText(String.valueOf(game.getHighScore()), 10, 50, highScorePaint);
        canvas.drawText(String.valueOf(game.getScore()), 10, 100, scorePaint);

    }
}
