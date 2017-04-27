package com.chiliahedron.fingertag.controllers;

import android.graphics.PointF;

import com.chiliahedron.fingertag.GameEngine;
import com.chiliahedron.fingertag.controllers.components.Velocity;
import com.chiliahedron.fingertag.models.Enemy;
import com.chiliahedron.fingertag.models.Entity;
import com.chiliahedron.fingertag.models.Player;

import java.util.Random;

public class EnemyController implements Controller {
    // TODO: Move attributes into the model.
    private static Random random;
    private GameEngine game;
    private Enemy enemy;
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
        Velocity vel = enemy.getVel();
        PointF velP = vel.getXY();
        if (x + velP.x < r || x + velP.x > game.getWidth() - r) {
            vel.bounceX();
        }
        if (y + velP.y < r || y + velP.y > game.getHeight() - r) {
            vel.bounceY();
        }
        enemy.step();
        Entity colliding = game.collidesWithEnemy(enemy);
        if (colliding != null) {
            vel.bounceXY();
            enemy.step();
        }
    }

    private void adjustVelocity() {
        Player player = game.getPlayer();
        float dx = random.nextFloat() * focus * Math.signum(player.getX() - enemy.getX());
        float dy = random.nextFloat() * focus * Math.signum(player.getY() - enemy.getY());
        enemy.getVel().offset(dx, dy);
    }
}
