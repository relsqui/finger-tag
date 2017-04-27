package com.chiliahedron.fingertag.models;

import com.chiliahedron.fingertag.controllers.components.Velocity;

public class MovingEntity extends Entity {
    private Velocity vel = new Velocity();

    MovingEntity(int radius, float x, float y) {
        super(radius, x, y);
    }

    public void step() {
        moveBy(vel.getXY());
    }

    public Velocity getVel() {
        return vel;
    }
}
