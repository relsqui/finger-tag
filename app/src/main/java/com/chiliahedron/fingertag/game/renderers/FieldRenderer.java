package com.chiliahedron.fingertag.game.renderers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FieldRenderer implements Renderer {
    private Paint bgPaint = new Paint();

    public FieldRenderer() {
        bgPaint.setColor(Color.BLACK);
        bgPaint.setStyle(Paint.Style.FILL);
    }
    public void render(Canvas canvas) {
        canvas.drawPaint(bgPaint);
    }
}
