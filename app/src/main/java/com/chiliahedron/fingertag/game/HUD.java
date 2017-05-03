/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;

import com.chiliahedron.fingertag.game.entities.renderers.Renderer;

/**
 * Renders game information for the user while the game is in progress.
 * <p>
 * This is an {@link OrientationEventListener} so that it can detect device rotations and render
 * in the appropriate part of the screen, while the display is otherwise locked to rotation changes
 * so that the much more precision-sensitive game content won't jump around.
 */
class HUD extends OrientationEventListener implements Renderer {
    private static final int TEXT_SIZE = 50;
    private static final int OUTLINE_THICKNESS = 10;
    private static final int MARGIN = 30;
    private static final int LIFE_SIZE = 40;
    private final float LINE_HEIGHT;
    private final RectF NATURAL;
    private final RectF SIDEWAYS;
    private int rotation;
    private int original;
    private RectF drawBox;
    private GameEngine game;
    private Paint scorePaint = new Paint();
    private Paint highScorePaint = new Paint();
    private Paint outline = new Paint();
    private Paint livesPaint = new Paint();

    /**
     * Create a HUD and initialize the Paints it will render information with.
     *
     * @param game  the {@link GameEngine} this describes.
     * @param context  the {@link GameActivity} which is managing the game.
     */
    HUD(GameEngine game, Context context) {
        super(context, SensorManager.SENSOR_DELAY_GAME);
        // Start listening for orientation changes.
        enable();

        this.game = game;
        float w = game.getWidth();
        float h = game.getHeight();
        float d = (h-w)/2;
        /* Because Canvas coordinates don't change when the canvas is rotated, these RectFs store
         * the actual shape of the rotated canvas. For this reason, elements in the HUD should
         * always be placed relative to the drawBox, not to the canvas. The fixX and fixY
         * methods automate that. */
        NATURAL = new RectF(0, 0, w, h);
        SIDEWAYS = new RectF(0 - d, d, w + d, h - d);
        drawBox = NATURAL;
        /* When the device is in its natural orientation, this does nothing because the actual
         * value of the rotation constant is 0, but if the game started while it was rotated
         * (e.g. phone in landscape), this corrects for that. Note that it only matters when the
         * device is in a rotated position but isn't currently moving (flat on a table). If it's
         * moving at all (being held up), the orientation will update immediately anyway. */
        original = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation() * 90;

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

    /**
     * Render the HUD.
     *
     * @param canvas  the {@link Canvas} to render it onto.
     */
    public void render(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotation, canvas.getWidth()/2, canvas.getHeight()/2);
        drawLives(canvas, livesPaint);
        topLeftText(canvas, 0, String.valueOf(game.getHighScore()), highScorePaint);
        topLeftText(canvas, 1, String.valueOf(game.getScore()), scorePaint);
        canvas.restore();
    }

    /**
     * Convenience method for placing text in rows in the top left of the screen.
     *
     * @param canvas  the {@link Canvas} to render onto.
     * @param row  what row of text to render (0 at the top, then 1, 2, and so on).
     * @param s  the {@link String} to render.
     * @param paint  what {@link Paint} to use to render the text.
     */
    private void topLeftText(Canvas canvas, int row, String s, Paint paint) {
        canvas.drawText(s, fixX(MARGIN), fixY((row + 1) * LINE_HEIGHT), outline);
        canvas.drawText(s, fixX(MARGIN), fixY((row + 1) * LINE_HEIGHT), paint);
    }

    /**
     * Draw the players' extra lives as circles in the top right corner of the screen.
     *
     * @param canvas  the {@link Canvas} to render the circles onto.
     * @param paint  the {@link Paint} to render them with.
     */
    private void drawLives(Canvas canvas, Paint paint) {
        float x = drawBox.right - MARGIN - LIFE_SIZE;
        float y = drawBox.top + MARGIN + LIFE_SIZE;
        int step = MARGIN + (2 * LIFE_SIZE);
        for (int i=0; i<game.getLives(); i++) {
            canvas.drawCircle(x, y, LIFE_SIZE, paint);
            x -= step;
        }
    }

    /**
     * When the device orientation changes, rotate the HUD accordingly.
     *
     * @param orientation  the new orientation, in degrees from the "natural" device position.
     */
    public void onOrientationChanged(int orientation) {
        if (orientation == ORIENTATION_UNKNOWN) return;
        orientation = (orientation + original) % 360;
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

    /**
     * Correct the desired x position for the current device orientation.
     *
     * @param x  the desired position
     * @return the corrected position
     */
    private float fixX(float x) {
        return x + drawBox.left;
    }

    /**
     * Correct the desired y position for the current device orientation.
     *
     * @param y  the desired position
     * @return the corrected position
     */
    private float fixY(float y) {
        return y + drawBox.top;
    }
}
