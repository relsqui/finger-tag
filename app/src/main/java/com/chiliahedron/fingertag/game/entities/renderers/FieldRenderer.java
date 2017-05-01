package com.chiliahedron.fingertag.game.entities.renderers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class FieldRenderer implements Renderer {
    private Paint bgPaint = new Paint();
    private Paint countdownPaint = new Paint();
    private int countdown = 0;

    public FieldRenderer() {
        bgPaint.setColor(Color.BLACK);
        bgPaint.setStyle(Paint.Style.FILL);
        countdownPaint.setColor(Color.DKGRAY);
        countdownPaint.setStyle(Paint.Style.FILL);
        countdownPaint.setTextSize(600);
    }
    public void render(Canvas canvas) {
        canvas.drawPaint(bgPaint);
        if (countdown != 0) {
            Rect bounds = new Rect();
            countdownPaint.getTextBounds(String.valueOf(countdown), 0, 1, bounds);
            canvas.drawText(String.valueOf(countdown), canvas.getWidth()/2 - bounds.width()/2, canvas.getHeight()/2 + bounds.height()/2, countdownPaint);
        }
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }
}
