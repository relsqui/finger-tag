package com.chiliahedron.fingertag.models;

import com.chiliahedron.fingertag.controllers.components.Velocity;

public class Enemy extends Entity {
    private Velocity vel = new Velocity();

    public Enemy(int radius, float x, float y) {
        super(radius, x, y);
    }

    public void step() {
        offsetXY(vel.getXY());
    }

    public Velocity getVel() {
        return vel;
    }

    public void setVel(float x, float y) {
        vel.set(x, y);
    }
}
