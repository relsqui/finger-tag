package com.chiliahedron.fingertag.game.controllers;

import android.graphics.PointF;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.models.components.Velocity;
import com.chiliahedron.fingertag.game.models.Enemy;
import com.chiliahedron.fingertag.game.models.Player;

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
        PointF posP = enemy.getXY();
        Velocity vel = enemy.getVel();
        PointF velP = vel.getXY();
        float r = enemy.getRadius();
        if (posP.x + velP.x < r || posP.x + velP.x > game.getWidth() - r) {
            vel.bounceX();
        }
        if (posP.y + velP.y < r || posP.y + velP.y > game.getHeight() - r) {
            vel.bounceY();
        }
        enemy.step();
        if (game.getEnemies().collideWith(enemy)) {
            vel.bounceXY();
            enemy.step();
        }
    }

    private void wander() {
        Player player = game.getPlayers().nearest(enemy);
        if (player == null) return;
        float dx = random.nextFloat() * enemy.getFocus() * Math.signum(player.getX() - enemy.getX());
        float dy = random.nextFloat() * enemy.getFocus() * Math.signum(player.getY() - enemy.getY());
        enemy.getVel().offset(dx, dy);
    }
}
