package net.prisoncore.core.modules.rankup.events;

import net.prisoncore.core.modules.rankup.RankupModule;
import net.prisoncore.core.modules.rankup.utils.RankupInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nonnull;

public final class InventoryClicks implements Listener {

    public InventoryClicks() {

    }

    @EventHandler
    public void onClick(@Nonnull final InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof RankupInventoryHolder) {
            e.setCancelled(true);
        }
    }
}
