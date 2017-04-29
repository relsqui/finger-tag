package com.chiliahedron.fingertag.game.models.powerups;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.models.Entity;
import com.chiliahedron.fingertag.game.models.MovingEntity;

abstract public class Powerup extends MovingEntity {
    private static final int DEFAULT_RADIUS = 15;

    Powerup(float x, float y) {
        super(DEFAULT_RADIUS, x, y);
        vel.setMax(10);
    }

    abstract public void collect(GameEngine game, Entity entity);
}