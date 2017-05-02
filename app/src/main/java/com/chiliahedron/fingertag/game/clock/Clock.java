package com.chiliahedron.fingertag.game.clock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Clock {
    private Random random;
    private int tick = 0;
    private List<TimedCommand> commands = new ArrayList<>();

    public Clock(Random random) {
        this.random = random;
    }

    public void add(Command command, int timer, int variation, boolean repeat) {
        commands.add(new TimedCommand(command, timer, variation, repeat, random, tick));
    }

    public void tick() {
        tick++;
        Iterator<TimedCommand> commandIterator = commands.iterator();
        while (commandIterator.hasNext()) {
            if (commandIterator.next().checkAndRun(tick)) {
                // checkAndRun returns true if the command is finished and non-repeating.
                commandIterator.remove();
            }
        }
    }
}
