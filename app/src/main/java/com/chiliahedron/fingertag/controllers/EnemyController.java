package com.chiliahedron.fingertag.controllers;

import android.util.Log;

import com.chiliahedron.fingertag.GameEngine;
import com.chiliahedron.fingertag.models.Entity;
import com.chiliahedron.fingertag.models.Player;

import java.util.Random;

public class EnemyController implements Controller {
    // TODO: Move velocity into the model.
    private static final String TAG = EnemyController.class.getSimpleName();
    private static Random random;
    private GameEngine game;
    private Entity enemy;
    private float maxSpeed = 15;       // Highest permitted absolute value of a velocity in px/tick
    private float focus = 10;          // How much should we focus on targeting the player
    private int wanderlust = 50;      // How often do we change directions
    private float xVel = 3;
    private float yVel = 3;

    public EnemyController(GameEngine game, Entity enemy) {
        this.game = game;
        this.enemy = enemy;
        random = game.getRandom();
        wanderlust += random.nextInt(50);
    }

    public void update() {
        if (game.getTick() % wanderlust == 0) {
            adjustVelocity();
        }
        float x = enemy.getX();
        float y = enemy.getY();
        float r = enemy.getRadius();
        if (x + xVel < r || x + xVel > game.getWidth() - r) {
            xVel *= -1;
        }
        if (y + yVel < r || y + yVel > game.getHeight() - r) {
            yVel *= -1;
        }
        enemy.setXY(x + xVel, y + yVel);
    }

    private void adjustVelocity() {
        Player player = game.getPlayer();
        xVel += random.nextFloat() * focus * Math.signum(player.getX() - enemy.getX());
        yVel += random.nextFloat() * focus * Math.signum(player.getY() - enemy.getY());
        xVel = Math.min(maxSpeed, Math.max(xVel, -1 * maxSpeed));
        yVel = Math.min(maxSpeed, Math.max(yVel, -1 * maxSpeed));
    }
}
