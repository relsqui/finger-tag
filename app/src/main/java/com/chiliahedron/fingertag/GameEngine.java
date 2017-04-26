package com.chiliahedron.fingertag;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import com.chiliahedron.fingertag.controllers.Controller;
import com.chiliahedron.fingertag.controllers.PlayerController;
import com.chiliahedron.fingertag.models.Entity;
import com.chiliahedron.fingertag.models.Player;
import com.chiliahedron.fingertag.views.EntityRenderer;
import com.chiliahedron.fingertag.views.FieldRenderer;
import com.chiliahedron.fingertag.views.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class GameEngine implements Controller, Renderer {
    private Player player;
    private List<Entity> enemies = new ArrayList<>();
    private FieldRenderer fieldRenderer;
    private EntityRenderer playerRenderer;
    private List<EntityRenderer> enemyRenderers;
    private PlayerController playerController;
    private static Random random = new Random(System.currentTimeMillis());


    GameEngine(int width, int height) {
        player = new Player(80, width/2, height/2);
        for (int i=0; i<3; i++) {
            Entity enemy = new Entity(80, 0, 0);
            do {
                int x = random.nextInt(width - enemy.getRadius() * 2) + enemy.getRadius();
                int y = random.nextInt(height - enemy.getRadius() * 2) + enemy.getRadius();
                enemy.setXY(x, y);
            } while(collidesWith(enemy) != null);
            enemies.add(enemy);
        }
        fieldRenderer = new FieldRenderer();
        playerRenderer = new EntityRenderer(player, Color.GREEN, Paint.Style.FILL);
        enemyRenderers = new ArrayList<>();
        for (Entity enemy : enemies) {
            enemyRenderers.add(new EntityRenderer(enemy, Color.RED, Paint.Style.STROKE));
        }
        playerController = new PlayerController(player);
    }

    private @Nullable
    Entity collidesWith(Entity e) {
        if (e.overlaps(player)) {
            return player;
        }
        for (Entity enemy : enemies) {
            if (e.overlaps(enemy)) {
                return enemy;
            }
        }
        return null;
    }

    public void update() {
        playerController.update();
    }

    public void render(Canvas canvas) {
        fieldRenderer.render(canvas);
        for (EntityRenderer e : enemyRenderers) {
            e.render(canvas);
        }
        playerRenderer.render(canvas);
    }

    boolean handleTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                playerController.handleActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                playerController.handleActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                playerController.handleActionUp(event);
                break;
        }
        return true;
    }
}
