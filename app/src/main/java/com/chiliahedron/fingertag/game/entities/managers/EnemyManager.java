/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.managers;

import android.graphics.Canvas;
import android.support.annotation.Nullable;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.entities.controllers.Controller;
import com.chiliahedron.fingertag.game.entities.controllers.EnemyController;
import com.chiliahedron.fingertag.game.entities.models.Enemy;
import com.chiliahedron.fingertag.game.entities.models.Entity;
import com.chiliahedron.fingertag.game.entities.renderers.EntityRenderer;
import com.chiliahedron.fingertag.game.entities.renderers.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Handles the appearance and disappearance of enemies. */
public class EnemyManager implements Controller, Renderer {
    private GameEngine game;
    private Random random;
    private int defaultSize;
    private List<Enemy> enemies = new ArrayList<>();
    private List<EntityRenderer> enemyRenderers = new ArrayList<>();
    private List<EnemyController> enemyControllers = new ArrayList<>();

    public EnemyManager(GameEngine game, int defaultSize) {
        this.game = game;
        this.random = game.getRandom();
        this.defaultSize = defaultSize;
    }

    /** Add a new enemy at a random location which isn't too close to a player. */
    public void add() {
        Enemy enemy = new Enemy(defaultSize, 0, 0);
        enemy.getVel().set(random.nextFloat() * 20 - 10, random.nextFloat() * 20 - 10);
        do {
            int x = random.nextInt(game.getWidth() - enemy.getRadius() * 2) + enemy.getRadius();
            int y = random.nextInt(game.getHeight() - enemy.getRadius() * 2) + enemy.getRadius();
            enemy.moveTo(x, y);
        } while(game.getPlayers().collideWith(enemy, 6 * defaultSize) || collideWith(enemy));
        enemies.add(enemy);
        enemyControllers.add(new EnemyController(game, enemy));
        enemyRenderers.add(new EntityRenderer(enemy));
    }

    /**
     * Find the nearest enemy to the given entity.
     *
     * @param e  the {@link Entity} to search near.
     * @return the closest {@link Enemy}, if one exists, or null if there aren't any.
     */
    @Nullable
    Enemy nearest(Entity e) {
        // Initialize minimum distance to the greatest possible distance between entities,
        // so any actual distance we find will be shorter.
        double minDistance = Math.max(game.getWidth(), game.getHeight());
        Enemy nearest = null;
        for (Enemy enemy : enemies) {
            double distance = e.distanceTo(enemy);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = enemy;
            }
        }
        return nearest;
    }

    /**
     * Remove the given enemy.
     *
     * @param enemy  the {@link Enemy} to remove.
     */
    void remove(Enemy enemy) {
        int index = enemies.indexOf(enemy);
        enemies.remove(index);
        enemyControllers.remove(index);
        enemyRenderers.remove(index);
    }

    /**
     * Check whether the given entity collides with any enemy.
     * @param e  the {@link Entity} to check collisions for.
     * @return true if the entity collides with any enemy, false otherwise.
     */
    public boolean collideWith(Entity e) {
        for (Entity enemy : enemies) {
            if (e.overlaps(enemy) && e != enemy) {
                return true;
            }
        }
        return false;
    }

    /** Remove all enemies. */
    public void clear() {
        enemies.clear();
        enemyControllers.clear();
        enemyRenderers.clear();
    }

    /** Update each enemy. */
    public void update() {
        for (EnemyController enemy : enemyControllers) {
            enemy.update();
        }
    }

    /**
     * Render all enemies.
     *
     * @param canvas  the {@link Canvas} to render them onto.
     */
    public void render(Canvas canvas) {
        for (EntityRenderer e : enemyRenderers) {
            e.render(canvas);
        }
    }
}
