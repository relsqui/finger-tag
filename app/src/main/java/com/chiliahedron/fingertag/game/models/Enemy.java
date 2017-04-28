package com.chiliahedron.fingertag.game.models;

public class Enemy extends MovingEntity {
    private int inertia = 0;    // How infrequently we change directions.
    private int focus = 0;      // How intently we chase the player.

    public Enemy(int radius, float x, float y) {
        super(radius, x, y);
    }

    public int getInertia() {
        return inertia;
    }

    public void setInertia(int inertia) {
        this.inertia = inertia;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }
}
