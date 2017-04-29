package com.chiliahedron.fingertag.game;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.view.Display;
import android.view.MotionEvent;

import com.chiliahedron.fingertag.game.controllers.EnemyController;
import com.chiliahedron.fingertag.game.controllers.PlayerController;
import com.chiliahedron.fingertag.game.models.Enemy;
import com.chiliahedron.fingertag.game.models.Entity;
import com.chiliahedron.fingertag.game.models.Player;
import com.chiliahedron.fingertag.game.views.EntityRenderer;
import com.chiliahedron.fingertag.game.views.FieldRenderer;
import com.chiliahedron.fingertag.game.views.HUD;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class GameEngine {
    private static final Random random = new Random(System.currentTimeMillis());
    private int width = 0;
    private int height = 0;
    private HUD hud;
    private FieldRenderer fieldRenderer;
    private static final int DEFAULT_SIZE = 60;
    private List<Player> players = new ArrayList<>();
    private List<EntityRenderer> playerRenderers = new ArrayList<>();
    private List<PlayerController> playerControllers = new ArrayList<>();
    private int[] playerColors = {Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.WHITE, Color.BLUE};
    private List<Enemy> enemies = new ArrayList<>();
    private List<EntityRenderer> enemyRenderers = new ArrayList<>();
    private List<EnemyController> enemyControllers = new ArrayList<>();
    private int highScore = 0;
    // TODO: Make score an array!
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
        // TODO: restore players!
    }

    private void addEnemy() {
        Enemy enemy = new Enemy(DEFAULT_SIZE, 0, 0);
        enemy.getVel().set(random.nextFloat() * 20 - 10, random.nextFloat() * 20 - 10);
        do {
            int x = random.nextInt(width - enemy.getRadius() * 2) + enemy.getRadius();
            int y = random.nextInt(height - enemy.getRadius() * 2) + enemy.getRadius();
            enemy.moveTo(x, y);
        } while(collidesWithPlayer(enemy, 6 * DEFAULT_SIZE) != null || collidesWithEnemy(enemy) != null);
        enemies.add(enemy);
        enemyControllers.add(new EnemyController(this, enemy));
        enemyRenderers.add(new EntityRenderer(enemy, Color.RED, Paint.Style.STROKE));
    }

    private void addPlayer(float x, float y) {
        Player player = new Player(DEFAULT_SIZE, x, y);
        playerRenderers.add(new EntityRenderer(player, playerColors[players.size()], Paint.Style.FILL));
        playerControllers.add(new PlayerController(this, player));
        players.add(player);
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

    @Nullable
    private Player collidesWithPlayer(Entity e, int buffer) {
        for (Player player : players) {
            if (player.overlaps(e, buffer) && e != player) {
                return player;
            }
        }
        return null;
    }

    boolean update() {
        // Return value is whether the game is over.
        if (players.size() == 0) return false;
        tick++;
        for (EnemyController enemy : enemyControllers) {
            enemy.update();
        }
        // TODO: Move the model/renderer into the controller, this is ridiculous.
        ListIterator<Player> playerIterator = players.listIterator();
        ListIterator<PlayerController> controllerIterator = playerControllers.listIterator();
        ListIterator<EntityRenderer> rendererIterator = playerRenderers.listIterator();
        // Using an iterator here instead of a for loop so we can modify the arrays on the fly.
        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();
            PlayerController controller = controllerIterator.next();
            // We don't actually need to store the renderer, just advance the iterator.
            rendererIterator.next();
            controller.update();
            if (collidesWithEnemy(player) != null) {
                playerIterator.remove();
                controllerIterator.remove();
                rendererIterator.remove();
            }
        }
        if (players.size() == 0) {
            return true;
        }
        if (tick % 50 == 0) {
            // TODO: increment each score, when they're an array!
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
        for (EntityRenderer p : playerRenderers) {
            p.render(canvas);
        }
        hud.render(canvas);
    }

    boolean handleTouchEvent(MotionEvent event) {
        int index = MotionEventCompat.getActionIndex(event);
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                boolean handled = false;
                for (PlayerController playerController : playerControllers) {
                    handled = handled || playerController.handleActionDown(event);
                }
                if (!handled) {
                    addPlayer(event.getX(index), event.getY(index));
                    playerControllers.get(playerControllers.size()-1).handleActionDown(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                for (PlayerController playerController : playerControllers) {
                    playerController.handleActionMove(event);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                for (PlayerController playerController : playerControllers) {
                    playerController.handleActionUp(event);
                }
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

    @Nullable
    public Player nearestPlayer(Entity e) {
        // Initialize minimum distance to the greatest possible distance between entities,
        // so any actual distance we find will be shorter.
        double minDistance = Math.max(width, height);
        Player nearest = null;
        for (Player player : players) {
            double distance = e.distanceTo(player);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = player;
            }
        }
        return nearest;
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
