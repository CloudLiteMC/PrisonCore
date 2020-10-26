package net.prisoncore.core.modules.essentials.scoreboard;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.UUID;

public final class ScoreboardModule extends Module {

    private final PrisonCore core;
    private HashMap<UUID, BukkitTask> playerScoreboards;

    public ScoreboardModule(@Nonnull final PrisonCore core) {
        this.core = core;
    }

    @Override
    public Module init() {
        this.playerScoreboards = new HashMap<>();
        return this;
    }

    @Override
    public void reload() {
        this.playerScoreboards.values().forEach(BukkitTask::cancel);
        this.playerScoreboards = new HashMap<>();
    }

    public final void loadScoreboard(@Nonnull final Player player) {
        final UUID uuid = player.getUniqueId();
    }
}
