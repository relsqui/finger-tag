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
        game.getClock().add(() -> {
            // Occasional large changes towards the target.
            float dx = random.nextFloat() * 8 * Math.signum(target.x - powerup.getX());
            float dy = random.nextFloat() * 8 * Math.signum(target.y - powerup.getY());
            powerup.getVel().offset(dx, dy);
        }, 55, 50, 10);
        game.getClock().add(() -> {
            // Frequent random small changes.
            float dx = random.nextFloat() * 2;
            float dy = random.nextFloat() * 2;
            powerup.getVel().offset(dx, dy);
        }, 20, 20, 5);
    }

    public void update() {
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
