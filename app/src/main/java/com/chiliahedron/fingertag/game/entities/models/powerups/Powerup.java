/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.models.powerups;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.entities.models.Entity;
import com.chiliahedron.fingertag.game.entities.models.MovingEntity;

/** A small {@link MovingEntity} which can be collected. */
abstract public class Powerup extends MovingEntity {
    private static final int DEFAULT_RADIUS = 15;

    Powerup(float x, float y) {
        super(DEFAULT_RADIUS, x, y);
    }

    /**
     * What happens when the powerup is collected (intercepted).
     *
     * @param game  the {@link GameEngine} in which this happened.
     * @param entity  the {@link Entity} which collected the powerup.
     */
    abstract public void collect(GameEngine game, Entity entity);
}