package net.prisoncore.core.modules.essentials.clearinventory.events;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.ModuleType;
import net.prisoncore.core.modules.essentials.clearinventory.ClearInventoryModule;
import net.prisoncore.core.modules.essentials.clearinventory.utils.ClearInventoryHolder;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.messages.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

public final class ConfirmClearRequest implements Listener {

    private final PrisonCore core;

    public ConfirmClearRequest(@Nonnull final PrisonCore core) {
        this.core = core;
    }

    @EventHandler
    public void onClick(@Nonnull final InventoryClickEvent e) {
        final Inventory inv = e.getInventory();
        if (inv.getHolder() instanceof ClearInventoryHolder) {
            e.setCancelled(true);
            final int slotClicked = e.getSlot();
            final Player p = (Player) e.getWhoClicked();
            final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());

            if (slotClicked == 11) { // yes
                ((ClearInventoryModule) this.core.getModule(ModuleType.CLEAR_INVENTORY)).clearInventory(user);
            } else if (slotClicked == 15) { // no
                user.getPlayer().closeInventory();
                Message.TARGET_INVENTORY_REQUEST_CANCELLED.sendMessage(user.getPlayer(), this.core);
            }
        }
    }

    @EventHandler
    public void onClose(@Nonnull final InventoryCloseEvent e) {
        final Inventory inv = e.getInventory();
        if (inv.getHolder() instanceof ClearInventoryHolder) {
            if (e.getReason().equals(InventoryCloseEvent.Reason.PLAYER)) {
                final Player p = (Player) e.getPlayer();
                Message.TARGET_INVENTORY_REQUEST_CANCELLED.sendMessage(p, this.core);
            }
        }
    }
}
