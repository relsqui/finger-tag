package com.chiliahedron.fingertag.models;

public class Player extends Entity {
    private boolean touch;

    public Player(int radius, float x, float y) {
        super(radius, x, y);
        touch = false;
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
