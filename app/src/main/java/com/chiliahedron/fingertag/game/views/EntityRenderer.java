package com.chiliahedron.fingertag.game.views;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.chiliahedron.fingertag.game.models.Entity;

public class EntityRenderer implements Renderer {
    private Entity e;
    private Paint paint = new Paint();
    private int color;

    public EntityRenderer(Entity e, int color, Paint.Style style) {
        this.e = e;
        this.color = color;
        paint.setColor(color);
        paint.setStyle(style);
        paint.setStrokeWidth(10);
    }

    public void render(Canvas canvas) {
        canvas.drawCircle(e.getX(), e.getY(), e.getRadius(), paint);
    }

    public int getColor() {
        return color;
    }
}
