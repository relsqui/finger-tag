package com.chiliahedron.fingertag.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

import com.chiliahedron.fingertag.GameEngine;

public class HUD implements Renderer {
    private GameEngine game;
    private Paint paint = new Paint();

    public HUD(GameEngine game) {
        this.game = game;
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
    }

    public void render(Canvas canvas) {
        canvas.drawText(String.valueOf(game.getScore()), 10, 50, paint);
    }
}
