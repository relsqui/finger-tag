package com.chiliahedron.fingertag.game;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.view.Display;
import android.view.MotionEvent;

import com.chiliahedron.fingertag.game.controllers.managers.EnemyManager;
import com.chiliahedron.fingertag.game.controllers.managers.PlayerManager;
import com.chiliahedron.fingertag.game.controllers.managers.PowerupManager;
import com.chiliahedron.fingertag.game.models.Entity;
import com.chiliahedron.fingertag.game.models.powerups.ExtraPoint;
import com.chiliahedron.fingertag.game.renderers.FieldRenderer;
import com.chiliahedron.fingertag.game.renderers.HUD;

import java.util.Random;

public class GameEngine {
    private static final Random random = new Random(System.currentTimeMillis());
    private static final int DEFAULT_SIZE = 60;
    private int width = 0;
    private int height = 0;
    private HUD hud;
    private FieldRenderer fieldRenderer;
    private PlayerManager players = new PlayerManager(this, DEFAULT_SIZE);
    private EnemyManager enemies = new EnemyManager(this, DEFAULT_SIZE);
    private PowerupManager powerups = new PowerupManager(this);
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
            enemies.add();
        }
    }

    boolean update() {
        // Returns true when the game has ended, false otherwise.
        if (players.size() == 0) {
            return false;
        }
        tick++;
        enemies.update();
        players.update();
        powerups.update();
        if (players.size() == 0) {
            // We had some until we updated, so if there are none left they've lost.
            return true;
        }
        if (tick % 50 == 0) {
            score++;
            if (score > highScore) {
                highScore = score;
            }
        }
        if (tick % 120 == 0) {
            powerups.add(new ExtraPoint(width/2, height/2));
        }
        if (score > enemies.size() * 5) {
            enemies.add();
        }
        return false;
    }

    void render(Canvas canvas) {
        fieldRenderer.render(canvas);
        enemies.render(canvas);
        powerups.render(canvas);
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
        powerups.clear();
    }

    public boolean visible(Entity e) {
        PointF pos = e.getXY();
        int radius = e.getRadius();
        return pos.x > radius && pos.y > radius && pos.x <= width-radius && pos.y < height-radius;
    }

    public PlayerManager getPlayers() {
        return players;
    }

    public EnemyManager getEnemies() {
        return enemies;
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

    void setHighScore(int score) { highScore = score; }

    public int getScore() {
        return score;
    }

    public void addScore(int score) { this.score += score; }

    public Random getRandom() {
        return random;
    }

    public long getTick() {
        return tick;
    }
}
