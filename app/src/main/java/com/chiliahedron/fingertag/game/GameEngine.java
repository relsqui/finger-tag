/*
 * Copyright (c) 2017 Finn Ellis.
 */

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
import com.chiliahedron.fingertag.game.entities.managers.EnemyManager;
import com.chiliahedron.fingertag.game.entities.managers.PlayerManager;
import com.chiliahedron.fingertag.game.entities.managers.PowerupManager;
import com.chiliahedron.fingertag.game.entities.models.Entity;
import com.chiliahedron.fingertag.game.entities.renderers.FieldRenderer;

import java.util.Random;

/** Manages the game logic, entities, and timing. */
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

    /**
     * Initialize fields and subcomponents before beginning the game.
     * <p>
     * This method isn't a constructor because many of those initializations require information
     * about the display which isn't available yet when this class is instantiated.
     *
     * @param context  The Activity which is managing the game.
     * @param display  The Display we're being rendered onto.
     * @see GameView#surfaceCreated(android.view.SurfaceHolder)
     */
    @TargetApi(17)
    public void setUp(Context context, Display display) {
        Point realSize = new Point();
        // Using getRealSize allows us to include the space behind the navigation bar.
        display.getRealSize(realSize);
        this.width = realSize.x;
        this.height = realSize.y;
        hud = new HUD(this, context);
        fieldRenderer = new FieldRenderer();
        players = new PlayerManager(this, DEFAULT_SIZE);
        enemies = new EnemyManager(this, DEFAULT_SIZE);
        powerups = new PowerupManager(this);
    }

    /** Configure all the timers that begin after the first player is added. */
    public void onFirstPlayer() {
        // The background countdown.
        fieldRenderer.setCountdown(3);
        clock.add(() -> fieldRenderer.setCountdown(2), 50, 0, 0);
        clock.add(() -> fieldRenderer.setCountdown(1), 100, 0, 0);
        clock.add(() -> fieldRenderer.setCountdown(0), 150, 0, 0);

        // Things that happen once after the countdown ends.
        clock.add(() -> joinOK = false, 150, 0, 0);
        clock.add(() -> {
            for (int i=0; i<3; i++) {
                enemies.add();
            }
        }, 150, 0, 0);

        // Things that happen periodically throughout the level.
        clock.add(() -> enemies.add(), 550, 400, 100);
        clock.add(() -> powerups.add(), 250, 400, 100);
        clock.add(() -> {
            score++;
            if (score > highScore) {
                highScore = score;
            }
        }, 200, 50, 0);
    }

    /**
     * Update the game state (move entities, check collisions, execute timers, and so on).
     *
     * @return true if the game is over, false if it should continue.
     */
    boolean update() {
        if (players.size() == 0) {
            // If we have no players yet but are updating, game hasn't started yet.
            // Don't proceed until we get a touch event, which will create a player.
            return false;
        }
        clock.tick();
        enemies.update();
        players.update();
        powerups.update();
        return players.size() == 0;
    }

    /**
     * Render all the visible game elements.
     *
     * @param canvas  the {@link Canvas} to render them onto.
     */
    void render(Canvas canvas) {
        fieldRenderer.render(canvas);
        enemies.render(canvas);
        powerups.render(canvas);
        players.render(canvas);
        hud.render(canvas);
    }

    /**
     * Catch touch events and pass them through to the player manager.
     *
     * @param event  The {@link MotionEvent} that occurred.
     * @return true to consume the event after we've handled it.
     */
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

    /** Clear the game state so it can be played again. */
    void clearState() {
        score = 0;
        lives = 3;
        enemies.clear();
        powerups.clear();
    }

    /**
     * Check whether an Entity is visible on the screen.
     *
     * @param e  an Entity.
     * @return true if the entity is visible, false otherwise.
     */
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

    public int getLives() { return lives; }

    public void addLives(int lives) { this.lives += lives; }

    public boolean getJoinOK() { return joinOK; }

    public Clock getClock() { return clock; }
}
