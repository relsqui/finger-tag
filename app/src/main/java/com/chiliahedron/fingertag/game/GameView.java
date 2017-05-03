/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

/** Receives player input and renders the screen. */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = GameView.class.getSimpleName();
    private GameThread thread;
    private GameEngine game;
    private boolean setupDone = false;

    /**
     * Create a GameView and register it as a {@link SurfaceHolder.Callback}.
     *
     * @param context  the {@link GameActivity} this view is displaying.
     */
    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    public void setGame(GameEngine game) {
        this.game = game;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    /** When this surface is ready, sets up the game and starts the game thread. */
    @TargetApi(17)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!setupDone) {
            game.setUp(getContext(), getDisplay());
            setupDone = true;
        }
        thread = new GameThread(getHolder(), (GameActivity) getContext());
        thread.setGame(game);
        thread.start();
    }

    /** When this surface is destroyed, tells the game thread to finish. */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.finish();
    }

    /** Passes touch events through to the game. */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return game.handleTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {}

    /** When this view has focus, set it to be immersively fullscreen. */
    @Override
    @TargetApi(19)
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    /**
     * Tell the game thread to finish and wait for it to do so.
     *
     * @see GameActivity#onPause()
     */
    void stopThread() {
        thread.finish();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.d(TAG, "Interrupted waiting for thread to die, don't care.");
        }
    }
}
