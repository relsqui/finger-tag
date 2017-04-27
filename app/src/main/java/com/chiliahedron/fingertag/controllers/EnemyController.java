package com.chiliahedron.fingertag.controllers;

import android.graphics.PointF;

import com.chiliahedron.fingertag.GameEngine;
import com.chiliahedron.fingertag.controllers.components.Velocity;
import com.chiliahedron.fingertag.models.Enemy;
import com.chiliahedron.fingertag.models.Entity;
import com.chiliahedron.fingertag.models.Player;

import java.util.Random;

public class EnemyController implements Controller {
    private static Random random;
    private GameEngine game;
    private Enemy enemy;

    public EnemyController(GameEngine game, Enemy enemy) {
        this.game = game;
        this.enemy = enemy;
        random = game.getRandom();
        enemy.setInertia(50 + random.nextInt(25));
        enemy.setFocus(5 + random.nextInt(10));
        Velocity vel = enemy.getVel();
        float max = vel.getMax();
        vel.set((random.nextFloat() * 2 * max) - max, (random.nextFloat() * 2 * max) - max);
    }

    public void update() {
        if (game.getTick() % enemy.getInertia() == 0) {
            wander();
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

    private void wander() {
        Player player = game.getPlayer();
        float dx = random.nextFloat() * enemy.getFocus() * Math.signum(player.getX() - enemy.getX());
        float dy = random.nextFloat() * enemy.getFocus() * Math.signum(player.getY() - enemy.getY());
        enemy.getVel().offset(dx, dy);
    }
}
