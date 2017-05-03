/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.models.powerups;

import android.graphics.Color;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.entities.models.Entity;

/** A white {@link Powerup} which awards a game point when collected. */
public class ExtraPoint extends Powerup {
    public ExtraPoint(float x, float y) {
        super(x, y);
        color = Color.WHITE;
    }

    @Override
    public void collect(GameEngine game, Entity entity) {
        game.addScore(1);
    }
}
