package com.chiliahedron.fingertag.controllers;

import android.view.MotionEvent;

import com.chiliahedron.fingertag.models.Player;

public class PlayerController implements Controller {
    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    public void update() {

    }

    public void handleActionDown(MotionEvent event) {
        if (player.contains(event.getX(), event.getY())) {
            player.touch();
        }
    }

    public void handleActionMove(MotionEvent event) {
        if (player.isTouched()) {
            player.setXY(event.getX(), event.getY());
        }
    }

    public void handleActionUp(MotionEvent event) {
        if (player.isTouched()) {
            player.drop();
        }
    }
}
