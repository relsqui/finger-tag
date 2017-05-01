package com.chiliahedron.fingertag.game.clock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Clock {
    private int tick = 0;
    private List<TimedCommand> commands = new ArrayList<>();

    void add(TimedCommand c) {
        c.calculateNextExecution(tick);
        commands.add(c);
    }

    void tick() {
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
