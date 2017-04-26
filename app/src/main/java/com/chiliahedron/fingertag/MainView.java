package com.chiliahedron.fingertag;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

public class MainView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = MainView.class.getSimpleName();
    private MainThread thread;
    private GameEngine game;

    public MainView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder());
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        game = new GameEngine(getWidth(), getHeight());
        thread.setGame(game);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.d(TAG, "Interrupted while shutting thread down, ignoring.");
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return game.handleTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {}

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
}