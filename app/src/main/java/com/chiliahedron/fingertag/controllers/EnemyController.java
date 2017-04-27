package com.chiliahedron.fingertag.controllers;

import android.graphics.PointF;

import com.chiliahedron.fingertag.GameEngine;
import com.chiliahedron.fingertag.models.Enemy;
import com.chiliahedron.fingertag.models.Entity;
import com.chiliahedron.fingertag.models.Player;

import java.util.Random;

public class EnemyController implements Controller {
    // TODO: Move attributes into the model.
    private static Random random;
    private GameEngine game;
    private Enemy enemy;
    private static final float maxSpeed = 15;       // Highest permitted abs(velocity), in px/tick
    private int focus;                              // How intensely we target the player
    private int wanderlust;                         // How often we change directions

    public EnemyController(GameEngine game, Enemy enemy) {
        this.game = game;
        this.enemy = enemy;
        random = game.getRandom();
        wanderlust = 50 + random.nextInt(25);
        focus = 5 + random.nextInt(10);
    }

    public void update() {
        if (game.getTick() % wanderlust == 0) {
            adjustVelocity();
        }
        float x = enemy.getX();
        float y = enemy.getY();
        float r = enemy.getRadius();
        PointF vel = enemy.getVel();
        if (x + vel.x < r || x + vel.x > game.getWidth() - r) {
            enemy.bounceX();
        }
        if (y + vel.y < r || y + vel.y > game.getHeight() - r) {
            enemy.bounceY();
        }
        enemy.step();
        Entity colliding = game.collidesWithEnemy(enemy);
        if (colliding != null) {
            enemy.bounceXY();
            enemy.step();
        }
    }

    private void adjustVelocity() {
        Player player = game.getPlayer();
        PointF vel = enemy.getVel();
        vel.x += random.nextFloat() * focus * Math.signum(player.getX() - enemy.getX());
        vel.y += random.nextFloat() * focus * Math.signum(player.getY() - enemy.getY());
        vel.x = Math.min(maxSpeed, Math.max(vel.x, -1 * maxSpeed));
        vel.y = Math.min(maxSpeed, Math.max(vel.y, -1 * maxSpeed));
        enemy.setVel(vel);
    }
}
