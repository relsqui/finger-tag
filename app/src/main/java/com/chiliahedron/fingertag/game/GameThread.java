package com.chiliahedron.fingertag.game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

class GameThread extends Thread {
    private static final String TAG = GameThread.class.getSimpleName();
    private static final int MAX_FPS = 50;
    private final long framePeriod;
    private final SurfaceHolder sh;
    private GameActivity gameActivity;
    private GameEngine game;
    private boolean finished = false;

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
