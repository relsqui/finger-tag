/*
 * Copyright (c) 2017 Finn Ellis.
 */

package com.chiliahedron.fingertag.game.clock;

import java.util.Random;

/** One clock event, including the command to be run and all its timing information. */
class TimedCommand {
    private Command command;
    private int variation;
    private int repeatEvery;
    private Random random;
    private int nextExecution = 0;

    /** @see Clock#add */
    TimedCommand(Command command, int firstTime, int repeatEvery, int variation, Random random, int tick) {
        this.command = command;
        this.variation = variation;
        this.repeatEvery = repeatEvery;
        this.random = random;
        nextExecution = tick + firstTime;
    }

    /**
     * Execute the command if and only if the current tick is correct.
     *
     * @param tick  the current clock tick.
     * @return true if the event was one-time and has now executed; false otherwise.
     */
    boolean checkAndRun(int tick) {
        if (tick == nextExecution) {
            command.execute();
            if (repeatEvery > 0) {
                calculateNextExecution(tick);
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Update the next execution time for this event.
     *
     * @param currentTick  the current clock tick.
     */
    private void calculateNextExecution(int currentTick) {
        nextExecution = currentTick + repeatEvery;
        if (variation > 0) {
            nextExecution += random.nextInt(variation * 2) - variation;
        }
    }
}