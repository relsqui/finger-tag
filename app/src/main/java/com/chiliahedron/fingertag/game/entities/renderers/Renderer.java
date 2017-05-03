/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.entities.renderers;

import android.graphics.Canvas;

/** Interface for any game object which renders onto a canvas. */
public interface Renderer {
    void render(Canvas canvas);
}