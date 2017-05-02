package com.chiliahedron.fingertag.game;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.view.Display;
import android.view.MotionEvent;

import com.chiliahedron.fingertag.game.clock.Clock;
import com.chiliahedron.fingertag.game.entities.controllers.managers.EnemyManager;
import com.chiliahedron.fingertag.game.entities.controllers.managers.PlayerManager;
import com.chiliahedron.fingertag.game.entities.controllers.managers.PowerupManager;
import com.chiliahedron.fingertag.game.entities.models.Entity;
import com.chiliahedron.fingertag.game.entities.renderers.FieldRenderer;

import java.util.Random;

public class GameEngine {
    private static final Random random = new Random(System.currentTimeMillis());
    private static final int DEFAULT_SIZE = 60;
    private int width = 0;
    private int height = 0;
    private Clock clock = new Clock(random);
    private HUD hud;
    private FieldRenderer fieldRenderer;
    private PlayerManager players;
    private EnemyManager enemies;
    private PowerupManager powerups;
    private boolean joinOK = true;
    private int highScore = 0;
    private int score = 0;
    private int lives = 3;
    private long tick = 0;

    @TargetApi(17)
    public void setUp(Context context, Display display) {
        Point realSize = new Point();
        display.getRealSize(realSize);
        this.width = realSize.x;
        this.height = realSize.y;
        // Initializing these down here because several of them need to know game dimensions.
        hud = new HUD(this, context);
        fieldRenderer = new FieldRenderer();
        players = new PlayerManager(this, DEFAULT_SIZE);
        enemies = new EnemyManager(this, DEFAULT_SIZE);
        powerups = new PowerupManager(this);
        clock.add(() -> hud.setDebug("foo"), 50, 0, false);
    }

    boolean update() {
        // Returns true when the game has ended, false otherwise.
        if (players.size() == 0) {
            return false;
        }
        clock.tick();
        if (tick < 150) {
            if (tick < 50) {
                fieldRenderer.setCountdown(3);
            } else if (tick < 100) {
                fieldRenderer.setCountdown(2);
            } else {
                fieldRenderer.setCountdown(1);
            }
            tick++;
            return false;
        } else if (tick == 150) {
            for (int i=0; i<3; i++) {
                enemies.add();
            }
        }
        fieldRenderer.setCountdown(0);
        joinOK = false;
        enemies.update();
        players.update();
        powerups.update();
        if (players.size() == 0) {
            // We had some until we updated, so if there are none left they've lost.
            return true;
        }
        if (tick != 0 && tick % 50 == 0) {
            score++;
            if (score > highScore) {
                highScore = score;
            }
        }
        if (tick != 0 && tick % 320 == 0) {
            powerups.add();
        }
        if (tick % 400 == 0) {
            // The +100 is so we get one on the first tick after the countdown.
            enemies.add();
        }
        tick++;
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
        lives = 3;
        enemies.clear();
        powerups.clear();
    }

    public boolean visible(Entity e) {
        PointF pos = e.getXY();
        int radius = e.getRadius();
        return pos.x > -1 * radius && pos.y > -1 * radius &&
               pos.x <= width + radius && pos.y < height + radius;
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

    int getHighScore() {
        return highScore;
    }

    void setHighScore(int score) { highScore = score; }

    int getScore() {
        return score;
    }

    public void addScore(int score) { this.score += score; }

    public Random getRandom() {
        return random;
    }

    public long getTick() {
        return tick;
    }

    public int getLives() { return lives; }

    public void addLives(int lives) { this.lives += lives; }

    public boolean getJoinOK() { return joinOK; }
}
