package net.prisoncore.core.modules.essentials.scoreboard.events;

import net.prisoncore.core.modules.essentials.scoreboard.ScoreboardModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;

public final class PlayerJoin implements Listener {

    public PlayerJoin(@Nonnull final ScoreboardModule module) {

    }

    @EventHandler
    public void onJoin(@Nonnull final PlayerJoinEvent e) {

    }

    @EventHandler
    public void onLeave(@Nonnull final PlayerQuitEvent e) {

    }
}
