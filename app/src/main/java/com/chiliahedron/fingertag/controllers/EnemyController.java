package com.chiliahedron.fingertag.controllers;

import com.chiliahedron.fingertag.GameEngine;
import com.chiliahedron.fingertag.models.Entity;
import com.chiliahedron.fingertag.models.Player;

import java.util.Random;

public class EnemyController implements Controller {
    // TODO: Move velocity into the model.
    private static Random random;
    private GameEngine game;
    private Entity enemy;
    private static final float maxSpeed = 15;       // Highest permitted abs(velocity), in px/tick
    private int focus;                              // How intensely we target the player
    private int wanderlust;                         // How often we change directions
    private float xVel;
    private float yVel;

    public EnemyController(GameEngine game, Entity enemy) {
        this.game = game;
        this.enemy = enemy;
        random = game.getRandom();
        wanderlust = 25 + random.nextInt(50);
        xVel = random.nextFloat() * 20 - 10;
        yVel = random.nextFloat() * 20 - 10;
        focus = 5 + random.nextInt(10);
    }

    public void update() {
        if (game.getTick() % wanderlust == 0) {
            adjustVelocity();
            wanderlust = 25 + random.nextInt(50);
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
        Entity colliding = game.collidesWithEnemy(enemy);
        if (colliding != null) {
            xVel *= -1;
            yVel *= -1;
        }
    }

    private void adjustVelocity() {
        Player player = game.getPlayer();
        xVel += random.nextFloat() * focus * Math.signum(player.getX() - enemy.getX());
        yVel += random.nextFloat() * focus * Math.signum(player.getY() - enemy.getY());
        xVel = Math.min(maxSpeed, Math.max(xVel, -1 * maxSpeed));
        yVel = Math.min(maxSpeed, Math.max(yVel, -1 * maxSpeed));
    }
}
