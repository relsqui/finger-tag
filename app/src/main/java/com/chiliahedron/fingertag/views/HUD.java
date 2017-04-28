package com.chiliahedron.fingertag.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;

import com.chiliahedron.fingertag.GameEngine;

public class HUD extends OrientationEventListener implements Renderer {
    private static final int TEXT_SIZE = 50;
    private static final int OUTLINE_THICKNESS = 10;
    private static final int MARGIN = 30;
    private final float LINE_HEIGHT;
    private int rotation;
    private GameEngine game;
    private Paint scorePaint = new Paint();
    private Paint highScorePaint = new Paint();
    private Paint outline = new Paint();

    public HUD(GameEngine game, Context context) {
        super(context, SensorManager.SENSOR_DELAY_GAME);
        enable();
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
        canvas.save();
        canvas.rotate(rotation, canvas.getWidth()/2, canvas.getHeight()/2);
        topLeftText(canvas, 0, String.valueOf(game.getHighScore()), highScorePaint);
        topLeftText(canvas, 1, String.valueOf(game.getScore()), scorePaint);
        PointF nw = new PointF(100, 100);
        PointF ne = new PointF(canvas.getWidth()-100, 100);
        PointF sw = new PointF(100, canvas.getHeight()-100);
        PointF se = new PointF(canvas.getWidth()-100, canvas.getHeight()-100);
        float[] lines = {
                nw.x, nw.y, ne.x, ne.y,
                ne.x, ne.y, se.x, se.y,
                se.x, se.y, sw.x, sw.y,
                sw.x, sw.y, nw.x, nw.y
        };
        canvas.drawLines(lines, highScorePaint);
        canvas.drawText("NW", nw.x, nw.y, scorePaint);
        canvas.drawText("NE", ne.x, ne.y, scorePaint);
        canvas.drawText("SW", sw.x, sw.y, scorePaint);
        canvas.drawText("SE", se.x, se.y, scorePaint);
        canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, 10, highScorePaint);
        canvas.drawText("500x900 rotated", 500, 900, highScorePaint);
        canvas.restore();
        canvas.drawText("500x900 normal", 500, 900, highScorePaint);
    }

    private void topLeftText(Canvas canvas, int row, String s, Paint paint) {
        canvas.drawText(s, MARGIN, (row + 1) * LINE_HEIGHT, outline);
        canvas.drawText(s, MARGIN, (row + 1) * LINE_HEIGHT, paint);
    }

    public void onOrientationChanged(int orientation) {
        if (orientation == ORIENTATION_UNKNOWN) return;
        if (orientation > 315 || orientation <=45) {
            // Right-side up.
            rotation = 0;
        } else if (orientation <= 135) {
            // Turned left, so rotate right.
            rotation = 270;
        } else if (orientation <= 225) {
            // Upside down.
            rotation = 180;
        } else {
            // Turned right, so rotate left.
            rotation = 90;
        }
    }
}
