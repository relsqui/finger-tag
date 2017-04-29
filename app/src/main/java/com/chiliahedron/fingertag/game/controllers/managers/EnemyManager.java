package com.chiliahedron.fingertag.game.controllers.managers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.controllers.EnemyController;
import com.chiliahedron.fingertag.game.models.Enemy;
import com.chiliahedron.fingertag.game.models.Entity;
import com.chiliahedron.fingertag.game.views.EntityRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyManager {
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
        enemyRenderers.add(new EntityRenderer(enemy, Color.RED, Paint.Style.STROKE));
    }

    public boolean collideWith(Entity e) {
        for (Entity enemy : enemies) {
            if (e.overlaps(enemy) && e != enemy) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        enemies.clear();
        enemyControllers.clear();
        enemyRenderers.clear();
    }

    public void update() {
        for (EnemyController enemy : enemyControllers) {
            enemy.update();
        }
    }

    public void render(Canvas canvas) {
        for (EntityRenderer e : enemyRenderers) {
            e.render(canvas);
        }
    }

    public int size() {
        return enemies.size();
    }
}
