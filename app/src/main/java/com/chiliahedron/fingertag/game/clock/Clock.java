/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.clock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** Manages timed events. */
public class Clock {
    private Random random;
    private int tick = 0;
    private List<TimedCommand> commands = new ArrayList<>();
    private List<TimedCommand> toAdd = new ArrayList<>();

    /**
     * Create a Clock.
     *
     * @param random the {@link Random} provided by the
           {@link com.chiliahedron.fingertag.game.GameEngine}.
     */
    public Clock(Random random) {
        this.random = random;
    }

    /**
     * Schedule an event.
     * <p>
     * All times are in ticks. One tick is
     * 1/{@link com.chiliahedron.fingertag.game.GameThread#MAX_FPS}. If the variation is specified,
     * every time the event time is calculated after the first, it will vary from repeatEvery by
     * an integer in between -variation and variation.
     *
     * @param command  the thing that should happen (probably a lambda or method reference).
     * @param firstTime  how many ticks from now should the event happen first?
     * @param repeatEvery  how often should it repeat? (0 for never)
     * @param variation  by how many ticks should the repeat time vary?
     */
    public void add(Command command, int firstTime, int repeatEvery, int variation) {
        // Queue adds to prevent concurrency errors when iterating later.
        toAdd.add(new TimedCommand(command, firstTime, repeatEvery, variation, random, tick));
    }

    /** Advances the clock by one tick and executes any appropriate events. */
    public void tick() {
        tick++;
        commands.addAll(toAdd);
        toAdd.clear();
        Iterator<TimedCommand> commandIterator = commands.iterator();
        while (commandIterator.hasNext()) {
            if (commandIterator.next().checkAndRun(tick)) {
                commandIterator.remove();
            }
        }
    }
}
