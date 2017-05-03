/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.models;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.chiliahedron.fingertag.game.entities.models.components.Position;

/**
 * A circle which has a position as well as a color and style.
 *
 * @see MovingEntity
 * @see com.chiliahedron.fingertag.game.entities.renderers.EntityRenderer
 */
public class Entity {
    final int radius;
    protected int color = Color.TRANSPARENT;
    Paint.Style style = Paint.Style.FILL;
    Position pos = new Position();

    /**
     * Create an Entity.
     *
     * @param radius  the size of the entity in dp.
     * @param x  its starting x position on the screen.
     * @param y  its starting y position on the screen.
     */
    Entity(int radius, float x, float y) {
        this.radius = radius;
        pos.set(x, y);
    }

    /**
     * Check if this entity overlaps another one.
     *
     * @param e  the other entity.
     * @return true if they overlap, false otherwise.
     */
    public boolean overlaps(Entity e) {
        return pos.distanceTo(e.getXY()) < e.getRadius() + radius;
    }

    /**
     * Check if this entity contains the given point.
     *
     * @param p  a {@link PointF}
     * @return true if point p is inside this entity, false otherwise.
     */
    public boolean contains(PointF p) {
        return pos.distanceTo(p) < radius;
    }

    /**
     * Calculate the distance from the center of this entity to the center of another entity.
     *
     * @param e  the other entity.
     * @return the distance between the entity centers in dp.
     */
    public double distanceTo(Entity e) { return pos.distanceTo(e.getXY()); }

    /**
     * Move this entity so its center is at the given coordinates.
     *
     * @param x  the new x coordinate.
     * @param y  the new y coordinate.
     */
    public void moveTo(float x, float y) {
        pos.set(x, y);
    }

    /**
     * Move this entity by an offset from its current position.
     *
     * @param point  a PointF representing the x and y offsets.
     */
    void moveBy(PointF point) {
        pos.offset(point);
    }

    public PointF getXY() {
        return pos.getXY();
    }

    public float getX() {
        return pos.getXY().x;
    }

    public float getY() {
        return pos.getXY().y;
    }

    public int getRadius() {
        return radius;
    }

    public int getColor() { return color; }

    public void setColor(int color) { this.color = color; }

    public Paint.Style getStyle() { return style; }
}
