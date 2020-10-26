package net.prisoncore.core.modules.essentials.motd.events;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.essentials.motd.ServerMotdModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import javax.annotation.Nonnull;

public class ServerPingEvent implements Listener {

    private final ServerMotdModule motdModule;

    public ServerPingEvent(@Nonnull final PrisonCore core,
                           @Nonnull final ServerMotdModule module) {
        this.motdModule = module;
    }

    @EventHandler
    public void onPing(@Nonnull final ServerListPingEvent e) {
        e.setMaxPlayers(e.getNumPlayers() + 1);
        e.setMotd(this.motdModule.getMotd());
    }
}
