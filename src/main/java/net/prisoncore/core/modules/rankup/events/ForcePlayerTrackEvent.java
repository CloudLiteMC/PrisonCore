package net.prisoncore.core.modules.rankup.events;

import net.luckperms.api.model.group.Group;
import net.prisoncore.core.modules.rankup.RankupModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;

import static net.prisoncore.core.utils.Utils.getLuckPermsGroup;
import static net.prisoncore.core.utils.Utils.sendConsoleCommand;

public final class ForcePlayerTrackEvent implements Listener {

    private final RankupModule module;

    public ForcePlayerTrackEvent(@Nonnull final RankupModule module) {
        this.module = module;
    }

    @EventHandler
    public void onJoin(@Nonnull final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final Group playerGroup = getLuckPermsGroup(p.getUniqueId());
        if (playerGroup.getName().equalsIgnoreCase("default")) {
            sendConsoleCommand(module.getDefaultRankCommand(p));
        }
    }
}
