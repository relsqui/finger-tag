package com.chiliahedron.fingertag.game.controllers;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.models.Player;
import com.chiliahedron.fingertag.game.models.powerups.Powerup;

import java.util.Random;

public class PowerupController implements Controller {
    private GameEngine game;
    private Random random;
    private Powerup powerup;
    private boolean collected = false;

    public PowerupController(GameEngine game, Powerup powerup) {
        this.game = game;
        this.random = game.getRandom();
        this.powerup = powerup;
        this.powerup.getVel().set(random.nextFloat() * 10 - 5, random.nextFloat() * 10 - 5);
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
}
