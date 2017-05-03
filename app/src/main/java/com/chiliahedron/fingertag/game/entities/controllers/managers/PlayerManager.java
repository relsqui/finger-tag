package com.chiliahedron.fingertag.game.entities.controllers.managers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.entities.controllers.Controller;
import com.chiliahedron.fingertag.game.entities.controllers.PlayerController;
import com.chiliahedron.fingertag.game.entities.models.Enemy;
import com.chiliahedron.fingertag.game.entities.models.Entity;
import com.chiliahedron.fingertag.game.entities.models.Player;
import com.chiliahedron.fingertag.game.entities.renderers.EntityRenderer;
import com.chiliahedron.fingertag.game.entities.renderers.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PlayerManager implements Controller, Renderer {
    private GameEngine game;
    private int defaultSize;
    private List<Player> players = new ArrayList<>();
    private List<EntityRenderer> playerRenderers = new ArrayList<>();
    private List<PlayerController> playerControllers = new ArrayList<>();
    // This is a map from (the opposites of) colors to their availability for a new player.
    private SparseBooleanArray colors = new SparseBooleanArray(6);

    public PlayerManager(GameEngine game, int defaultSize) {
        this.game = game;
        this.defaultSize = defaultSize;
        colors.put(Color.GREEN, true);
        colors.put(Color.MAGENTA, true);
        colors.put(Color.YELLOW, true);
        colors.put(Color.CYAN, true);
        colors.put(Color.BLUE, true);
        colors.put(Color.WHITE, true);
    }

    private void add(float x, float y) {
        int availableIndex = colors.indexOfValue(true);
        // indexOfValue returns a negative if it can't find an appropriate key.
        if (availableIndex < 0) return;
        int color = colors.keyAt(availableIndex);
        colors.put(color, false);
        Player player = new Player(defaultSize, x, y);
        player.setColor(color);
        playerRenderers.add(new EntityRenderer(player));
        playerControllers.add(new PlayerController(game, player));
        players.add(player);
    }

    public void update() {
        ListIterator<Player> playerIterator = players.listIterator();
        ListIterator<PlayerController> controllerIterator = playerControllers.listIterator();
        ListIterator<EntityRenderer> rendererIterator = playerRenderers.listIterator();
        // Using an iterator here instead of a for loop so we can modify the arrays on the fly.
        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();
            PlayerController controller = controllerIterator.next();
            // We don't need to store the renderer, just keep the iterators in sync.
            rendererIterator.next();
            controller.update();
            Enemy nearestEnemy = game.getEnemies().nearest(player);
            if (nearestEnemy != null && nearestEnemy.overlaps(player)) {
                if (game.getLives() > 0) {
                    game.addLives(-1);
                    game.getEnemies().remove(nearestEnemy);
                } else {
                    playerIterator.remove();
                    controllerIterator.remove();
                    rendererIterator.remove();
                    // Free up this player's color for another player.
                    colors.put(player.getColor(), true);
                }
            }
        }
    }

    boolean collideWith(Entity e, int buffer) {
        for (Player player : players) {
            if (player.overlaps(e, buffer) && e != player) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public Player nearest(Entity e) {
        // Initialize minimum distance to the greatest possible distance between entities,
        // so any actual distance we find will be shorter.
        double minDistance = Math.max(game.getWidth(), game.getHeight());
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

    public void render(Canvas canvas) {
        for (EntityRenderer p : playerRenderers) {
            p.render(canvas);
        }
    }

    public void handleActionDown(MotionEvent event) {
        int index = MotionEventCompat.getActionIndex(event);
        boolean handled = false;
        for (PlayerController playerController : playerControllers) {
            handled = handled || playerController.handleActionDown(event);
        }
        if (!handled && game.getJoinOK()) {
            add(event.getX(index), event.getY(index));
            playerControllers.get(playerControllers.size()-1).handleActionDown(event);
            if (players.size() == 1) {
                game.onFirstPlayer();
            }
        }
    }

    public void handleActionMove(MotionEvent event) {
        for (PlayerController playerController : playerControllers) {
            playerController.handleActionMove(event);
        }
    }

    public void handleActionUp(MotionEvent event) {
        for (PlayerController playerController : playerControllers) {
            playerController.handleActionUp(event);
        }
    }

    public int size() {
        return players.size();
    }
}
