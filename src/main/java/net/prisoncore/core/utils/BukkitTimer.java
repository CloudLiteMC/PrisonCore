package net.prisoncore.core.utils;

import net.prisoncore.core.PrisonCore;

import javax.annotation.Nonnull;

public abstract class BukkitTimer {

    private PrisonCore core;
    private final int timeToRun;
    private int timeLeft = 0;

    public BukkitTimer(@Nonnull final PrisonCore core,
                       final int timeToRun) {
        this.core = core;
        this.timeToRun = timeToRun;
    }

    public abstract void createTimer();

    public final int getTimeLeft() {
        return this.timeLeft;
    }
}
