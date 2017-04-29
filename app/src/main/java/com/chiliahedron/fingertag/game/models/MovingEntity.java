package com.chiliahedron.fingertag.game.models;

import com.chiliahedron.fingertag.game.models.components.Velocity;

public class MovingEntity extends Entity {
    protected Velocity vel = new Velocity();

    public MovingEntity(int radius, float x, float y) {
        super(radius, x, y);
    }

    public void step() {
        moveBy(vel.getXY());
    }

    public Velocity getVel() {
        return vel;
    }
}
