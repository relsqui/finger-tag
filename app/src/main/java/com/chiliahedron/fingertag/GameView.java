package com.chiliahedron.fingertag;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = GameView.class.getSimpleName();
    private GameThread thread;
    private GameEngine game;
    private boolean setupDone = false;

    public GameView(Context context) {
        super(context);
        Log.d(TAG, "Created view.");
        getHolder().addCallback(this);
        setFocusable(true);
    }

    public void setGame(GameEngine game) {
        this.game = game;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @TargetApi(17)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Surface created, setting up game and thread.");
        if (!setupDone) {
            game.setUp(getContext(), getDisplay());
            setupDone = true;
        }
        thread = new GameThread(getHolder());
        thread.setGame(game);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface destroyed, finishing thread.");
        thread.finish();
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

    void stopThread() {
        thread.finish();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.d(TAG, "Interrupted waiting for thread to die.");
        }
    }
}