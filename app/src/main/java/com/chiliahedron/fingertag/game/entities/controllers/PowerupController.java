package com.chiliahedron.fingertag.game.entities.controllers;

import android.graphics.PointF;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.entities.models.Player;
import com.chiliahedron.fingertag.game.entities.models.powerups.Powerup;

import java.util.Random;

public class PowerupController implements Controller {
    private GameEngine game;
    private Random random;
    private Powerup powerup;
    private boolean collected = false;
    private PointF target = new PointF(0, 0);

    public PowerupController(GameEngine game, Powerup powerup) {
        this.game = game;
        this.powerup = powerup;
        random = game.getRandom();
    }

    public void update() {
        float dx = 0;
        float dy = 0;
        if (game.getTick() % 55 == 0) {
            // Occasional large changes towards the target.
            dx += random.nextFloat() * 8 * Math.signum(target.x - powerup.getX());
            dy += random.nextFloat() * 8 * Math.signum(target.y - powerup.getY());
        }
        if (game.getTick() % 20 == 0) {
            // Frequent random small changes.
            dx += random.nextFloat() * 3;
            dy += random.nextFloat() * 3;
        }
        powerup.getVel().offset(dx, dy);
        powerup.step();
        Player nearestPlayer = game.getPlayers().nearest(powerup);
        if (nearestPlayer != null && nearestPlayer.overlaps(powerup)) {
            powerup.collect(game, nearestPlayer);
            collected = true;
        }
    }

    public boolean wasCollected() {
        return collected;
    }

    public Powerup getPowerup() {
        return powerup;
    }

    public void setTarget(PointF target) {
        this.target.set(target);
    }
}
