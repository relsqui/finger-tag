package com.chiliahedron.fingertag.game.controllers;

import android.graphics.PointF;
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
            player.touch();
        }
    }

    public void handleActionMove(MotionEvent event) {
        if (player.isTouched() && player.contains(new PointF(event.getX(), event.getY()))) {
            float r = player.getRadius();
            float x = Math.max(Math.min(event.getX(), game.getWidth() - r), r);
            float y = Math.max(Math.min(event.getY(), game.getHeight() - r), r);
            player.moveTo(x, y);
        }
    }

    public void handleActionUp(MotionEvent event) {
        if (player.isTouched() && player.contains(new PointF(event.getX(), event.getY()))) {
            player.drop();
        }
    }
}
