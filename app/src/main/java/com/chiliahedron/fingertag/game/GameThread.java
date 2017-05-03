/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/** Asynchronously runs the main game loop. */
class GameThread extends Thread {
    private static final String TAG = GameThread.class.getSimpleName();
    private static final int MAX_FPS = 50;
    private final long framePeriod;
    private final SurfaceHolder sh;
    private GameActivity gameActivity;
    private GameEngine game;
    private boolean finished = false;

    /**
     * Create a GameThread and determine the framePeriod from the FPS.
     *
     * @param sh  a {@link SurfaceHolder} for the surface onto which the game will be rendered.
     * @param gameActivity  the {@link GameActivity} which is managing the game.
     */
    GameThread(SurfaceHolder sh, GameActivity gameActivity) {
        super();
        this.sh = sh;
        this.gameActivity = gameActivity;
        framePeriod = 1000000000L / MAX_FPS;
        Log.d(TAG, "Initializing thread.");
    }

    void setGame(GameEngine game) {
        this.game = game;
    }

    /**
     * Start the thread.
     * <p>
     * Once per framePeriod (1/MAX_FPS), this method does the following:
     * <ul>
     *     <li>Calls {@link GameEngine#update()}
     *     <li>Locks the canvas provided by surfaceHolder
     *     <li>Calls {@link GameEngine#render(Canvas)} on that canvas
     *     <li>Sleeps for any remaining time in the framePeriod.
     * </ul>
     * This loop ends when something calls {@link #finish()}.
     */
    @Override
    public void run() {
        Log.d(TAG, "Starting thread run.");
        while (!finished) {
            long startTime = System.nanoTime();
            Canvas canvas = null;
            try {
                canvas = sh.lockCanvas();
                synchronized (sh) {
                    finished = game.update();
                    game.render(canvas);
                }
            } finally {
                if (canvas != null) {
                    sh.unlockCanvasAndPost(canvas);
                }
            }
            long extraNanoseconds = framePeriod - (System.nanoTime() - startTime);
            if (extraNanoseconds > 0) {
                try {
                    Thread.sleep(extraNanoseconds / 1000000);
                } catch (InterruptedException e) {
                    Log.d(TAG, "Interrupted while sleeping, don't care.");
                }
            }
        }
        gameActivity.onGameFinished();
    }

    void finish() {
        finished = true;
    }
}
