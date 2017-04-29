package com.chiliahedron.fingertag.game.controllers.managers;

import android.graphics.Canvas;

import com.chiliahedron.fingertag.game.GameEngine;
import com.chiliahedron.fingertag.game.controllers.Controller;
import com.chiliahedron.fingertag.game.controllers.PowerupController;
import com.chiliahedron.fingertag.game.models.powerups.Powerup;
import com.chiliahedron.fingertag.game.renderers.EntityRenderer;
import com.chiliahedron.fingertag.game.renderers.Renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PowerupManager implements Controller, Renderer {
    private GameEngine game;
    private List<PowerupController> controllers = new ArrayList<>();
    private List<EntityRenderer> renderers = new ArrayList<>();

    public PowerupManager(GameEngine game) {
        this.game = game;
    }

    public void add(Powerup powerup) {
        controllers.add(new PowerupController(game, powerup));
        renderers.add(new EntityRenderer(powerup));
    }

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

    public void render(Canvas canvas) {
        for (EntityRenderer renderer : renderers) {
            renderer.render(canvas);
        }
    }

    public void clear() {
        controllers.clear();
        renderers.clear();
    }
}
