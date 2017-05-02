package com.chiliahedron.fingertag.game.clock;

import java.util.Random;

class TimedCommand {
    private Command command;
    private int timer;
    private int variation;
    private boolean repeat;
    private Random random;
    private int nextExecution = 0;

    TimedCommand(Command command, int timer, int variation, boolean repeat, Random random, int tick) {
        this.command = command;
        this.timer = timer;
        this.variation = variation;
        this.repeat = repeat;
        this.random = random;
        calculateNextExecution(tick);
    }

    boolean checkAndRun(int tick) {
        if (tick == nextExecution) {
            command.execute();
            if (repeat) {
                calculateNextExecution(tick);
            } else {
                return true;
            }
        }
        return false;
    }

    private void calculateNextExecution(int currentTick) {
        nextExecution = currentTick + timer + random.nextInt(variation * 2) - variation;
    }
}