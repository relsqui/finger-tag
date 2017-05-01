package com.chiliahedron.fingertag.game.entities.models.components;

import android.graphics.PointF;

public class Position {
    private float x = 0;
    private float y = 0;

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointF getXY() {
        return new PointF(x, y);
    }

    public void offset(PointF p) {
        x += p.x;
        y += p.y;
    }

    public double distanceTo(PointF p) {
        return Math.sqrt(Math.pow(p.x-x, 2) + Math.pow(p.y-y, 2));
    }
}
