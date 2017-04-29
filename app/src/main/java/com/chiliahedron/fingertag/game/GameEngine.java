package com.chiliahedron.fingertag.game;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.view.Display;
import android.view.MotionEvent;

import com.chiliahedron.fingertag.game.controllers.EnemyController;
import com.chiliahedron.fingertag.game.controllers.PlayerManager;
import com.chiliahedron.fingertag.game.models.Enemy;
import com.chiliahedron.fingertag.game.models.Entity;
import com.chiliahedron.fingertag.game.models.Player;
import com.chiliahedron.fingertag.game.views.EntityRenderer;
import com.chiliahedron.fingertag.game.views.FieldRenderer;
import com.chiliahedron.fingertag.game.views.HUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {
    private static final Random random = new Random(System.currentTimeMillis());
    private int width = 0;
    private int height = 0;
    private HUD hud;
    private FieldRenderer fieldRenderer;
    private static final int DEFAULT_SIZE = 60;
    private PlayerManager players = new PlayerManager(this, DEFAULT_SIZE);
    private List<Enemy> enemies = new ArrayList<>();
    private List<EntityRenderer> enemyRenderers = new ArrayList<>();
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
        for (int i=0; i<score; i+=5) {
            // This comes up if we're recreating a game after a pause.
            addEnemy();
        }
    }

    private void addEnemy() {
        Enemy enemy = new Enemy(DEFAULT_SIZE, 0, 0);
        enemy.getVel().set(random.nextFloat() * 20 - 10, random.nextFloat() * 20 - 10);
        do {
            int x = random.nextInt(width - enemy.getRadius() * 2) + enemy.getRadius();
            int y = random.nextInt(height - enemy.getRadius() * 2) + enemy.getRadius();
            enemy.moveTo(x, y);
        } while(players.collideWith(enemy, 6 * DEFAULT_SIZE) || collidesWithEnemy(enemy));
        enemies.add(enemy);
        enemyControllers.add(new EnemyController(this, enemy));
        enemyRenderers.add(new EntityRenderer(enemy, Color.RED, Paint.Style.STROKE));
    }

     public boolean collidesWithEnemy(Entity e) {
        for (Entity enemy : enemies) {
            if (e.overlaps(enemy) && e != enemy) {
                return true;
            }
        }
        return false;
    }

    boolean update() {
        // Return value is whether the game is over.
        if (players.size() == 0) return false;
        tick++;
        for (EnemyController enemy : enemyControllers) {
            enemy.update();
        }
        players.update();
        if (players.size() == 0) {
            return true;
        }
        if (tick % 50 == 0) {
            score++;
            if (score > highScore) highScore = score;
        }
        if (score > enemies.size() * 5) {
            addEnemy();
        }
        return false;
    }

    void render(Canvas canvas) {
        fieldRenderer.render(canvas);
        for (EntityRenderer e : enemyRenderers) {
            e.render(canvas);
        }
        players.render(canvas);
        hud.render(canvas);
    }

    boolean handleTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                players.handleActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                players.handleActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                players.handleActionUp(event);
                break;
        }
        return true;
    }

    void clearState() {
        score = 0;
        enemies.clear();
        enemyControllers.clear();
        enemyRenderers.clear();
    }

    public Player nearestPlayer(Entity e) {
        return players.nearest(e);
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
}
