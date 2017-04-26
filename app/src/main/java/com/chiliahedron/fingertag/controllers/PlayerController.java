package com.chiliahedron.fingertag.controllers;

import android.view.MotionEvent;

import com.chiliahedron.fingertag.GameEngine;
import com.chiliahedron.fingertag.models.Player;

public class PlayerController implements Controller {
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
            float r = player.getRadius();
            float x = Math.max(Math.min(event.getX(), game.getWidth() - r), 0);
            float y = Math.max(Math.min(event.getY(), game.getHeight() - r), 0);
            player.setXY(x, y);
        }
    }

    public void handleActionUp(MotionEvent event) {
        if (player.isTouched()) {
            player.drop();
        }
    }
}
