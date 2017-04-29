package com.chiliahedron.fingertag.game.models;

import android.graphics.Paint;

public class Player extends Entity {
    private int touch = -1;

    public Player(int radius, float x, float y) {
        super(radius, x, y);
    }

    public boolean overlaps(Entity e, int buffer) {
        return pos.distanceTo(e.getXY()) < e.getRadius() + radius + buffer;
    }

    public int touchedBy() {
        return touch;
    }

    public void touchWith(int pointerId) {
        touch = pointerId;
    }

    public void drop() {
        touch = -1;
    }
}
