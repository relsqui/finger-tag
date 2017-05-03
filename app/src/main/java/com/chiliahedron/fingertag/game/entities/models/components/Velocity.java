/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.models.components;

import android.graphics.PointF;

/**
 * A velocity component for {@link com.chiliahedron.fingertag.game.entities.models.Entity}s.
 *
 * @see com.chiliahedron.fingertag.game.entities.models.MovingEntity
 */
public class Velocity {
    private float x = 0;
    private float y = 0;
    private float maxSpeed = 15;

    public PointF getXY() {
        return new PointF(x, y);
    }

    public void set(float x, float y) {
        this.x = Math.min(maxSpeed, Math.max(-1 * maxSpeed, x));
        this.y = Math.min(maxSpeed, Math.max(-1 * maxSpeed, y));
    }

    public void offset(float dx, float dy) {
        // Using set instead of direct access so we respect the speed cap.
        set(x + dx, y + dy);
    }

    /** Reverse the direction of the x component. */
    public void bounceX() {
        x *= -1;
    }

    /** Reverse the direction of the y component. */
    public void bounceY() {
        y *= -1;
    }

    /** Reverse direction in both dimensions. */
    public void bounceXY() {
        x *= -1;
        y *= -1;
    }

    public float getMax() {
        return maxSpeed;
    }
}
