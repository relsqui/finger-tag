package com.chiliahedron.fingertag.game.models.powerups;

import android.graphics.Color;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.models.Entity;

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
