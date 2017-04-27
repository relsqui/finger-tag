package com.chiliahedron.fingertag.models;

import android.graphics.PointF;

public class Enemy extends Entity {
    private PointF vel;

    public Enemy(int radius, float x, float y) {
        super(radius, x, y);
        vel = new PointF(0, 0);
    }

    public PointF getVel() {
        return vel;
    }

    public void setVel(float x, float y) {
        vel.set(x, y);
    }

    public void offsetVel(float dx, float dy) {
        vel.offset(dx, dy);
    }
}
