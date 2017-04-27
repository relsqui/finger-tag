package com.chiliahedron.fingertag.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.chiliahedron.fingertag.GameEngine;

public class HUD implements Renderer {
    private static final int TEXT_SIZE = 50;
    private static final int OUTLINE_THICKNESS = 10;
    private static final int MARGIN = 30;
    private final float LINE_HEIGHT;
    private GameEngine game;
    private Paint scorePaint = new Paint();
    private Paint highScorePaint = new Paint();
    private Paint outline = new Paint();

    public HUD(GameEngine game) {
        this.game = game;
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(TEXT_SIZE);
        highScorePaint.setColor(Color.YELLOW);
        highScorePaint.setTextSize(TEXT_SIZE);
        outline.setColor(Color.BLACK);
        outline.setTextSize(TEXT_SIZE);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(OUTLINE_THICKNESS);
        outline.setStrokeJoin(Paint.Join.ROUND);

        // http://stackoverflow.com/a/42091739/252125
        Paint.FontMetrics fm = outline.getFontMetrics();
        LINE_HEIGHT = fm.bottom - fm.top + fm.leading;
    }

    public void render(Canvas canvas) {
        topLeftText(canvas, 0, String.valueOf(game.getHighScore()), highScorePaint);
        topLeftText(canvas, 1, String.valueOf(game.getScore()), scorePaint);
    }

    private void topLeftText(Canvas canvas, int row, String s, Paint paint) {
        canvas.drawText(s, MARGIN, (row + 1) * LINE_HEIGHT, outline);
        canvas.drawText(s, MARGIN, (row + 1) * LINE_HEIGHT, paint);
    }
}
