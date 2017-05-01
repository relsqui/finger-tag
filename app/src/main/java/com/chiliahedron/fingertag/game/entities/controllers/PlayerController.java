package com.chiliahedron.fingertag.game.entities.controllers;

import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.entities.models.Player;

public class PlayerController implements Controller {
    private GameEngine game;
    private Player player;

    public PlayerController(GameEngine game, Player player) {
        this.game = game;
        this.player = player;
    }

    public void update() {}

    public boolean handleActionDown(MotionEvent event) {
        // Return value is whether we handled this.
        int index = MotionEventCompat.getActionIndex(event);
        if (player.contains(new PointF(event.getX(index), event.getY(index)))) {
            player.touchWith(event.getPointerId(index));
            return true;
        }
        return false;
    }

    public void handleActionMove(MotionEvent event) {
        int pointerId = player.touchedBy();
        if (pointerId == -1) return;
        int index = event.findPointerIndex(player.touchedBy());
        float r = player.getRadius();
        float x = Math.max(Math.min(event.getX(index), game.getWidth() - r), r);
        float y = Math.max(Math.min(event.getY(index), game.getHeight() - r), r);
        player.moveTo(x, y);
    }

    public void handleActionUp(MotionEvent event) {
        int pointerId = event.getPointerId(MotionEventCompat.getActionIndex(event));
        if (player.touchedBy() == pointerId) {
            player.drop();
        }
    }
}
