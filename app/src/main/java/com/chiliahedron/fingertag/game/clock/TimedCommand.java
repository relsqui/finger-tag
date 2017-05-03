package com.chiliahedron.fingertag.game.clock;

import java.util.Random;

class TimedCommand {
    private Command command;
    private int firstTime;
    private int variation;
    private int repeatEvery;
    private Random random;
    private int nextExecution = 0;

    TimedCommand(Command command, int firstTime, int repeatEvery, int variation, Random random, int tick) {
        this.command = command;
        this.firstTime = firstTime;
        this.variation = variation;
        this.repeatEvery = repeatEvery;
        this.random = random;
        nextExecution = tick + firstTime;
    }

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

    private void calculateNextExecution(int currentTick) {
        nextExecution = currentTick + repeatEvery;
        if (variation > 0) {
            nextExecution += random.nextInt(variation * 2) - variation;
        }
    }
}