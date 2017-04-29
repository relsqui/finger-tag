package com.chiliahedron.fingertag.game.renderers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;

import com.chiliahedron.fingertag.game.GameEngine;

public class HUD extends OrientationEventListener implements Renderer {
    private static final int TEXT_SIZE = 50;
    private static final int OUTLINE_THICKNESS = 10;
    private static final int MARGIN = 30;
    private static final int LIFE_SIZE = 40;
    private final float LINE_HEIGHT;
    private final RectF NATURAL;
    private final RectF SIDEWAYS;
    private int rotation;
    private RectF drawBox;
    private GameEngine game;
    private Paint scorePaint = new Paint();
    private Paint highScorePaint = new Paint();
    private Paint outline = new Paint();
    private Paint livesPaint = new Paint();

    public HUD(GameEngine game, Context context) {
        super(context, SensorManager.SENSOR_DELAY_GAME);
        enable();
        this.game = game;
        float w = game.getWidth();
        float h = game.getHeight();
        float d = (h-w)/2;
        NATURAL = new RectF(0, 0, w, h);
        SIDEWAYS = new RectF(0 - d, d, w + d, h - d);
        drawBox = NATURAL;
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(TEXT_SIZE);
        highScorePaint.setColor(Color.YELLOW);
        highScorePaint.setTextSize(TEXT_SIZE);
        outline.setColor(Color.BLACK);
        outline.setTextSize(TEXT_SIZE);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(OUTLINE_THICKNESS);
        outline.setStrokeJoin(Paint.Join.ROUND);
        livesPaint.setColor(Color.GRAY);
        livesPaint.setStyle(Paint.Style.FILL);

        // http://stackoverflow.com/a/42091739/252125
        Paint.FontMetrics fm = outline.getFontMetrics();
        LINE_HEIGHT = fm.bottom - fm.top + fm.leading;
    }

    public void render(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotation, canvas.getWidth()/2, canvas.getHeight()/2);
        drawLives(canvas, livesPaint);
        topLeftText(canvas, 0, String.valueOf(game.getHighScore()), highScorePaint);
        topLeftText(canvas, 1, String.valueOf(game.getScore()), scorePaint);
        canvas.restore();
    }

    private void topLeftText(Canvas canvas, int row, String s, Paint paint) {
        canvas.drawText(s, fixX(MARGIN), fixY((row + 1) * LINE_HEIGHT), outline);
        canvas.drawText(s, fixX(MARGIN), fixY((row + 1) * LINE_HEIGHT), paint);
    }

    private void drawLives(Canvas canvas, Paint paint) {
        float x = drawBox.right - MARGIN - LIFE_SIZE;
        float y = drawBox.top + MARGIN + LIFE_SIZE;
        int step = MARGIN + (2 * LIFE_SIZE);
        for (int i=0; i<game.getLives(); i++) {
            canvas.drawCircle(x, y, LIFE_SIZE, paint);
            x -= step;
        }
    }

    public void onOrientationChanged(int orientation) {
        if (orientation == ORIENTATION_UNKNOWN) return;
        if (orientation > 315 || orientation <=45) {
            // Right-side up.
            rotation = 0;
            drawBox = NATURAL;
        } else if (orientation <= 135) {
            // Turned left, so rotate right.
            rotation = 270;
            drawBox = SIDEWAYS;
        } else if (orientation <= 225) {
            // Upside down.
            rotation = 180;
            drawBox = NATURAL;
        } else {
            // Turned right, so rotate left.
            rotation = 90;
            drawBox = SIDEWAYS;
        }
    }

    private float fixX(float x) {
        return x + drawBox.left;
    }

    private float fixY(float y) {
        return y + drawBox.top;
    }
}
