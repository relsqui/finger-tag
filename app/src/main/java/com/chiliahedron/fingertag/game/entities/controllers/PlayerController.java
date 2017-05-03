/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.controllers;

import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.entities.models.Player;

/** Translates player input into movement of the player entity. */
public class PlayerController implements Controller {
    private GameEngine game;
    private Player player;

    /**
     * Create a PlayerController.
     *
     * @param game  the {@link GameEngine} this player is in.
     * @param player  the {@link Player} entity being controlled.
     */
    public PlayerController(GameEngine game, Player player) {
        this.game = game;
        this.player = player;
    }

    public void update() {}

    /**
     * Respond to a touch by updating the pointer touching this player.
     *
     * @param event  the {@link MotionEvent} which occurred, including location and pointer.
     * @return true if this method consumed the touch event, false otherwise.
     */
    public boolean handleActionDown(MotionEvent event) {
        int index = MotionEventCompat.getActionIndex(event);
        if (player.contains(new PointF(event.getX(index), event.getY(index)))) {
            player.touchWith(event.getPointerId(index));
            return true;
        }
        return false;
    }

    /**
     * Handle a touch movement by moving the player, if appropriate.
     *
     * @param event  the {@link MotionEvent} which occurred.
     */
    public void handleActionMove(MotionEvent event) {
        int pointerId = player.touchedBy();
        if (pointerId == -1) return;
        int index = event.findPointerIndex(player.touchedBy());
        /*
        Don't drag outside the game view. The components are evaluated separately so that the
        player can be dragged along an edge rather than stopping dead while near one.
         */
        float r = player.getRadius();
        float x = Math.max(Math.min(event.getX(index), game.getWidth() - r), r);
        float y = Math.max(Math.min(event.getY(index), game.getHeight() - r), r);
        player.moveTo(x, y);
    }

    /**
     * Handle a touch release by updating the pointer touching this player, if appropriate.
     *
     * @param event  the {@link MotionEvent} which occurred.
     */
    public void handleActionUp(MotionEvent event) {
        int pointerId = event.getPointerId(MotionEventCompat.getActionIndex(event));
        if (player.touchedBy() == pointerId) {
            player.drop();
        }
    }
}
