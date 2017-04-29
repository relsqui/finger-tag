package com.chiliahedron.fingertag.game.renderers;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.chiliahedron.fingertag.game.models.Entity;

public class EntityRenderer implements Renderer {
    private Entity e;
    private Paint paint = new Paint();

    public EntityRenderer(Entity e) {
        this.e = e;
        paint.setColor(e.getColor());
        paint.setStyle(e.getStyle());
        paint.setStrokeWidth(10);
    }

    public void render(Canvas canvas) {
        canvas.drawCircle(e.getX(), e.getY(), e.getRadius(), paint);
    }
}
