package com.chiliahedron.fingertag.models;

import android.graphics.PointF;

public class Enemy extends Entity {
    // TODO: Move all the velocity stuff into a component!
    private PointF vel;

    public Enemy(int radius, float x, float y) {
        super(radius, x, y);
        vel = new PointF(0, 0);
    }

    public void step() {
        x += vel.x;
        y += vel.y;
    }

    public PointF getVel() {
        return vel;
    }

    public void setVel(float x, float y) {
        vel.set(x, y);
    }

    public void setVel(PointF vel) {
        this.vel = vel;
    }

    public void bounceX() {
        vel.set(vel.x * -1, vel.y);
    }

    public void bounceY() {
        vel.set(vel.x, vel.y * -1);
    }

    public void bounceXY() {
        bounceX();
        bounceY();
    }
}
