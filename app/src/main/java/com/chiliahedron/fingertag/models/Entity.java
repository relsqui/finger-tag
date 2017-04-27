package com.chiliahedron.fingertag.models;


public class Entity {
    final int radius;
    float x;
    float y;

    public Entity(int radius, float x, float y) {
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

    public void addXY(float x, float y) {
        this.x += x;
        this.y += y;
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
