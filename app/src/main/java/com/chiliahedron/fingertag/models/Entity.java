package com.chiliahedron.fingertag.models;


import android.graphics.PointF;

public class Entity {
    final int radius;
    float x;
    float y;

    Entity(int radius, float x, float y) {
        this.radius = radius;
        this.x = x;
        this.y = y;
    }

    public boolean overlaps(Entity e) {
        return Math.sqrt(Math.pow(e.getX()-x, 2) + Math.pow(e.getY()-y, 2)) < e.getRadius()+radius;
    }

    public boolean contains(float x, float y) {
        return Math.sqrt(Math.pow(x-this.x, 2) + Math.pow(y-this.y, 2)) < radius;
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    void offsetXY(PointF point) {
        this.x += point.x;
        this.y += point.y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }
}
