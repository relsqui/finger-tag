/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.models;

import com.chiliahedron.fingertag.game.entities.models.components.Velocity;

/**
 * An {@link Entity} which has a {@link Velocity}.
 *
 * @see Enemy
 * @see com.chiliahedron.fingertag.game.entities.models.powerups.Powerup
 */
public class MovingEntity extends Entity {
    private Velocity vel = new Velocity();

    public MovingEntity(int radius, float x, float y) {
        super(radius, x, y);
    }

    /** Increment the entity's position by its current velocity. */
    public void step() {
        moveBy(vel.getXY());
    }

    public Velocity getVel() {
        return vel;
    }
}
