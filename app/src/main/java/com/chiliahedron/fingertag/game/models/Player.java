package com.chiliahedron.fingertag.game.models;

public class Player extends Entity {
    private boolean touch;

    public Player(int radius, float x, float y) {
        super(radius, x, y);
        touch = false;
    }

    public boolean overlaps(Entity e, int buffer) {
        return pos.distanceFrom(e.getXY()) < e.getRadius() + radius + buffer;
    }

    public boolean isTouched() {
        return touch;
    }

    public void touch() {
        touch = true;
    }

    public void drop() {
        touch = false;
    }
}
