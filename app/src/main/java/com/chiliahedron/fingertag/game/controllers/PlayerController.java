package com.chiliahedron.fingertag.game.controllers;

import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.models.Player;

public class PlayerController implements Controller {
    private GameEngine game;
    private Player player;

    public PlayerController(GameEngine game, Player player) {
        this.game = game;
        this.player = player;
    }

    public void update() {}

    public void handleActionDown(MotionEvent event) {
        if (player.contains(new PointF(event.getX(), event.getY()))) {
            player.touchWith(event.getPointerId(MotionEventCompat.getActionIndex(event)));
        }
    }

    public void handleActionMove(MotionEvent event) {
        int pointerId = event.getPointerId(MotionEventCompat.getActionIndex(event));
        if (player.touchedBy() == pointerId) {
            float r = player.getRadius();
            float x = Math.max(Math.min(event.getX(), game.getWidth() - r), r);
            float y = Math.max(Math.min(event.getY(), game.getHeight() - r), r);
            player.moveTo(x, y);
        }
    }

    public void handleActionUp(MotionEvent event) {
        int pointerId = event.getPointerId(MotionEventCompat.getActionIndex(event));
        if (player.touchedBy() == pointerId) {
            player.drop();
        }
    }
}
