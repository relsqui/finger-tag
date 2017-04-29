package com.chiliahedron.fingertag.game.models.powerups;

import android.graphics.Color;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.models.Entity;

public class SpawnEnemy extends Powerup {
    public SpawnEnemy(float x, float y) {
        super(x, y);
        color = Color.RED;
    }

    public void collect(GameEngine game, Entity entity) {
        game.getEnemies().add();
    }
}