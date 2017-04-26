package com.chiliahedron.fingertag.controllers;

import android.util.Log;
import android.view.MotionEvent;

import com.chiliahedron.fingertag.GameEngine;
import com.chiliahedron.fingertag.models.Player;

public class PlayerController implements Controller {
    private static final String TAG = PlayerController.class.getSimpleName();
    private GameEngine game;
    private Player player;

    public PlayerController(GameEngine game, Player player) {
        this.game = game;
        this.player = player;
    }

    public void update() {}

    public void handleActionDown(MotionEvent event) {
        if (player.contains(event.getX(), event.getY())) {
            player.touch();
        }
    }

    public void handleActionMove(MotionEvent event) {
        if (player.isTouched()) {
            float x = event.getX();
            float y = event.getY();
            float r = player.getRadius();
            float maxX = game.getWidth() - r;
            float maxY = game.getHeight() - r;
            if (x < r) x = r;
            if (x > maxX) x = maxX;
            if (y < r) y = r;
            if (y > maxY) y = maxY;
            player.setXY(x, y);
        }
    }

    public void handleActionUp(MotionEvent event) {
        if (player.isTouched()) {
            player.drop();
        }
    }
}
