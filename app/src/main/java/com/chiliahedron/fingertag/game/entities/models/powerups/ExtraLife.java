package com.chiliahedron.fingertag.game.entities.models.powerups;

import android.graphics.Color;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.entities.models.Entity;

public class ExtraLife extends Powerup {
    public ExtraLife(float x, float y) {
        super(x, y);
        color = Color.GRAY;
    }

    public void collect(GameEngine game, Entity entity) {
        game.addLives(1);
    }
}
