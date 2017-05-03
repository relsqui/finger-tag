/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.models.powerups;

import android.graphics.Color;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.entities.models.Entity;

/** A red {@link Powerup} which spawns an enemy when collected. */
public class SpawnEnemy extends Powerup {
    public SpawnEnemy(float x, float y) {
        super(x, y);
        color = Color.RED;
    }

    public void collect(GameEngine game, Entity entity) {
        game.getEnemies().add();
    }
}