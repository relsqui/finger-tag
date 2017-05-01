package com.chiliahedron.fingertag.game.entities.models.powerups;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.entities.models.Entity;
import com.chiliahedron.fingertag.game.entities.models.MovingEntity;

abstract public class Powerup extends MovingEntity {
    private static final int DEFAULT_RADIUS = 15;

    Powerup(float x, float y) {
        super(DEFAULT_RADIUS, x, y);
    }

    abstract public void collect(GameEngine game, Entity entity);
}