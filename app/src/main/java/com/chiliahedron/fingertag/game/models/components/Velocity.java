package com.chiliahedron.fingertag.game.models.components;

import android.graphics.PointF;

public class Velocity {
    private float x = 0;
    private float y = 0;
    private float maxSpeed = 10;

    public PointF getXY() {
        return new PointF(x, y);
    }

    public void set(float x, float y) {
        this.x = Math.min(maxSpeed, Math.max(-1 * maxSpeed, x));
        this.y = Math.min(maxSpeed, Math.max(-1 * maxSpeed, y));
    }

    public void offset(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    public void bounceX() {
        x *= -1;
    }

    public void bounceY() {
        y *= -1;
    }

    public void bounceXY() {
        bounceX();
        bounceY();
    }

    public float getMax() {
        return maxSpeed;
    }

    public void setMax(float maxSpeed) { this.maxSpeed = maxSpeed; }
}
