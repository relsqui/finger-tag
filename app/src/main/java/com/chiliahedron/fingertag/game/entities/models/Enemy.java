/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.models;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * A {@link MovingEntity} which bounces off walls and pursues the player.
 *
 * @see com.chiliahedron.fingertag.game.entities.controllers.EnemyController
 * @see com.chiliahedron.fingertag.game.entities.managers.EnemyManager
 */
public class Enemy extends MovingEntity {
    /** How infrequently this enemy moves. */
    private int inertia = 0;
    /** How intently this enemy chases the player. */
    private int focus = 0;

    public Enemy(int radius, float x, float y) {
        super(radius, x, y);
        color = Color.RED;
        style = Paint.Style.STROKE;
    }

    public int getInertia() {
        return inertia;
    }

    public void setInertia(int inertia) {
        this.inertia = inertia;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }
}
