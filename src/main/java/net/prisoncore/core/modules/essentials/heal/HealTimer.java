package net.prisoncore.core.modules.essentials.heal;

import net.prisoncore.core.PrisonCore;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.UUID;

public class HealTimer {

    private final BukkitTask task;

    private int timeToRun;
    private int timeRunningFor;

    public HealTimer(@Nonnull final HealModule module,
                     @Nonnull final UUID timerOwner) {
        PrisonCore core = module.getCore();
        this.timeToRun = module.getHealCooldown();
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
