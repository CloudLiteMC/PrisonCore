package net.prisoncore.core.modules.essentials.feed;

import net.prisoncore.core.PrisonCore;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.UUID;

public class FeedTimer {
    private final BukkitTask task;

    private int timeToRun;
    private int timeRunningFor;

    public FeedTimer(@Nonnull final FeedModule module,
                     @Nonnull final UUID timerOwner) {
        PrisonCore core = module.getCore();
        this.timeToRun = module.getFeedCooldown();
        this.timeRunningFor = 0;
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                if (timeRunningFor >= timeToRun) {
                    module.clearTimer(timerOwner);
                } else {
                    timeRunningFor += 1;
                }
            }
        }.runTaskTimerAsynchronously(core, 20, 20);
    }

    public final int getTimeLeft() {
        return this.timeToRun - this.timeRunningFor;
    }

    public void clear() {
        this.task.cancel();
    }
}
