/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.models;

/** A player-controlled {@link Entity}. */
public class Player extends Entity {
    /** The ID of the pointer currently touching this entity during a touch event. */
    private int touch = -1;

    public Player(int radius, float x, float y) {
        super(radius, x, y);
    }

    /**
     * Check whether another entity is closer than a specified buffer around this player.
     *
     * @param e  the entity to compare to.
     * @param buffer  the size of the buffer, in dp.
     * @return true if the entity is inside the buffer, false otherwise.
     */
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
