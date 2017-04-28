package com.chiliahedron.fingertag;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.MotionEvent;

import com.chiliahedron.fingertag.controllers.Controller;
import com.chiliahedron.fingertag.controllers.EnemyController;
import com.chiliahedron.fingertag.controllers.PlayerController;
import com.chiliahedron.fingertag.models.Enemy;
import com.chiliahedron.fingertag.models.Entity;
import com.chiliahedron.fingertag.models.Player;
import com.chiliahedron.fingertag.views.EntityRenderer;
import com.chiliahedron.fingertag.views.FieldRenderer;
import com.chiliahedron.fingertag.views.HUD;
import com.chiliahedron.fingertag.views.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine implements Controller, Renderer {
    private static final Random random = new Random(System.currentTimeMillis());
    private int width = 0;
    private int height = 0;
    private Player player;
    private List<Enemy> enemies = new ArrayList<>();
    private FieldRenderer fieldRenderer;
    private EntityRenderer playerRenderer;
    private List<EntityRenderer> enemyRenderers = new ArrayList<>();
    private HUD hud;
    private PlayerController playerController;
    private List<EnemyController> enemyControllers = new ArrayList<>();
    private int highScore = 0;
    private int score = 0;
    private long tick = 0;

    @TargetApi(17)
    public void setUp(Context context, Display display) {
        Point realSize = new Point();
        display.getRealSize(realSize);
        this.width = realSize.x;
        this.height = realSize.y;
        hud = new HUD(this, context);
        fieldRenderer = new FieldRenderer();
        player = new Player(70, width/2, height/2);
        playerRenderer = new EntityRenderer(player, Color.GREEN, Paint.Style.FILL);
        playerController = new PlayerController(this, player);
        for (int i=0; i<score; i+=5) {
            // This comes up if we're recreating a game after a pause.
            addEnemy();
        }
    }

    private void addEnemy() {
        Enemy enemy = new Enemy(70, 0, 0);
        enemy.getVel().set(random.nextFloat() * 20 - 10, random.nextFloat() * 20 - 10);
        do {
            int x = random.nextInt(width - enemy.getRadius() * 2) + enemy.getRadius();
            int y = random.nextInt(height - enemy.getRadius() * 2) + enemy.getRadius();
            enemy.moveTo(x, y);
        } while(player.overlaps(enemy, 200) || collidesWithEnemy(enemy) != null);
        enemies.add(enemy);
        enemyControllers.add(new EnemyController(this, enemy));
        enemyRenderers.add(new EntityRenderer(enemy, Color.RED, Paint.Style.STROKE));
    }

    @Nullable
     public Entity collidesWithEnemy(Entity e) {
        for (Entity enemy : enemies) {
            if (e.overlaps(enemy) && e != enemy) {
                return enemy;
            }
        }
        return null;
    }

    public void update() {
        tick++;
        playerController.update();
        for (EnemyController enemy : enemyControllers) {
            enemy.update();
        }
        if (collidesWithEnemy(player) != null) {
            score = 0;
            enemies.clear();
            enemyControllers.clear();
            enemyRenderers.clear();
            addEnemy();
        } else if (tick % 50 == 0) {
            score++;
            if (score > highScore) highScore = score;
        }
        if (score > enemies.size() * 5) {
            addEnemy();
        }
    }

    public void render(Canvas canvas) {
        fieldRenderer.render(canvas);
        for (EntityRenderer e : enemyRenderers) {
            e.render(canvas);
        }
        playerRenderer.render(canvas);
        hud.render(canvas);
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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getHighScore() {
        return highScore;
    }

    void setScore(int score) { this.score = score; }
    void setHighScore(int score) { highScore = score; }

    public int getScore() {
        return score;
    }

    public Random getRandom() {
        return random;
    }

    public long getTick() {
        return tick;
    }

    public Player getPlayer() {
        return player;
    }
}
