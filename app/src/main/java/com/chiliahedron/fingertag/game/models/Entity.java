package com.chiliahedron.fingertag.game.models;

import android.graphics.PointF;

import com.chiliahedron.fingertag.game.models.components.Position;

public class Entity {
    final int radius;
    Position pos = new Position();

    Entity(int radius, float x, float y) {
        this.radius = radius;
        pos.set(x, y);
    }

    public boolean overlaps(Entity e) {
        return pos.distanceTo(e.getXY()) < e.getRadius() + radius;
    }

    public boolean contains(PointF p) {
        return pos.distanceTo(p) < radius;
    }

    public double distanceTo(Entity e) { return pos.distanceTo(e.getXY()); }

    public PointF getXY() {
        return pos.getXY();
    }

    public void moveTo(float x, float y) {
        pos.set(x, y);
    }

    void moveBy(PointF point) {
        pos.offset(point);
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
}
