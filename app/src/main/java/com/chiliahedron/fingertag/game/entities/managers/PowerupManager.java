/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.managers;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.entities.controllers.Controller;
import com.chiliahedron.fingertag.game.entities.controllers.PowerupController;
import com.chiliahedron.fingertag.game.entities.models.powerups.ExtraLife;
import com.chiliahedron.fingertag.game.entities.models.powerups.ExtraPoint;
import com.chiliahedron.fingertag.game.entities.models.powerups.Powerup;
import com.chiliahedron.fingertag.game.entities.models.powerups.SpawnEnemy;
import com.chiliahedron.fingertag.game.entities.renderers.EntityRenderer;
import com.chiliahedron.fingertag.game.entities.renderers.Renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** Handles the appearance and disappearance of powerups. */
public class PowerupManager implements Controller, Renderer {
    private GameEngine game;
    private Random random;
    private List<PowerupController> controllers = new ArrayList<>();
    private List<EntityRenderer> renderers = new ArrayList<>();
    private PointF[] startPos = new PointF[8];
    private PointF[] startVel = new PointF[8];
    private PointF[] target = new PointF[8];

    public PowerupManager(GameEngine game) {
        this.game = game;
        random = game.getRandom();
        float w = game.getWidth();
        float h = game.getHeight();

        // Start in one corner, move towards the opposite corner (before drift).
        startPos[0] = new PointF(20, 20);
        startVel[0] = new PointF(5 * w/h, 5);
        target[0] = new PointF(w + 20, h + 20);

        startPos[1] = new PointF(w-20, 20);
        startVel[1] = new PointF(-5 * w/h, 5);
        target[1] = new PointF(-20, h+20);

        startPos[2] = new PointF(20, h-20);
        startVel[2] = new PointF(5 * w/h, -5);
        target[2] = new PointF(w+20, -20);

        startPos[3] = new PointF(w-20, h-20);
        startVel[3] = new PointF(-5 * w/h, -5);
        target[3] = new PointF(-20, -20);

        // Start on one side, move to the opposite side.
        startPos[4] = new PointF(w/2, 20);
        startVel[4] = new PointF(0, 5);
        target[4] = new PointF(w/2, h+20);

        startPos[5] = new PointF(20, h/2);
        startVel[5] = new PointF(5, 0);
        target[5] = new PointF(w+20, h/2);

        startPos[6] = new PointF(w/2, h-20);
        startVel[6] = new PointF(0, -5);
        target[6] = new PointF(w/2, -20);

        startPos[7] = new PointF(w-20, h/2);
        startVel[7] = new PointF(-5, 0);
        target[7] = new PointF(-20, h/2);
    }

    /** Spawn a powerup, chosen at (weighted) random. */
    public void add() {
        int startIndex = random.nextInt(8);
        Powerup powerup;
        int powerupChoice = random.nextInt(100);
        if (powerupChoice < 60) {
            powerup = new ExtraPoint(startPos[startIndex].x, startPos[startIndex].y);
        } else if (powerupChoice < 90) {
            powerup = new SpawnEnemy(startPos[startIndex].x, startPos[startIndex].y);
        } else {
            powerup = new ExtraLife(startPos[startIndex].x, startPos[startIndex].y);
        }
        powerup.getVel().set(startVel[startIndex].x, startVel[startIndex].y);
        controllers.add(new PowerupController(game, powerup));
        controllers.get(controllers.size()-1).setTarget(target[startIndex]);
        renderers.add(new EntityRenderer(powerup));
    }

    /** Update each powerup, removing any that have been collected or gone offscreen. */
    public void update() {
        Iterator<PowerupController> powerupIterator = controllers.iterator();
        Iterator<EntityRenderer> rendererIterator = renderers.iterator();
        while (powerupIterator.hasNext()) {
            PowerupController controller = powerupIterator.next();
            rendererIterator.next();
            controller.update();
            if (controller.wasCollected() || !game.visible(controller.getPowerup())) {
                powerupIterator.remove();
                rendererIterator.remove();
            }
        }
    }

    /**
     * Render all the powerups.
     *
     * @param canvas  the {@link Canvas} to render them onto.
     */
    public void render(Canvas canvas) {
        for (EntityRenderer renderer : renderers) {
            renderer.render(canvas);
        }
    }

    /** Remove all the powerups. */
    public void clear() {
        controllers.clear();
        renderers.clear();
    }
}
