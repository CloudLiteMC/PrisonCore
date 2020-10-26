package net.prisoncore.core.modules.tools.pickaxe.events;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;

import static net.prisoncore.core.utils.Utils.isPickaxe;

public final class PreventPickaxeRemoval implements Listener {

    private final PrisonCore core;
    private final HashMap<Player, ItemStack> playerPickaxesWhoDied;

    public PreventPickaxeRemoval(@Nonnull final PrisonCore core) {
        this.core = core;
        this.playerPickaxesWhoDied = new HashMap<>();
    }

    @EventHandler
    public void onRespawn(@Nonnull final PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        p.getInventory().setItem(0, this.playerPickaxesWhoDied.get(p));
        this.playerPickaxesWhoDied.remove(p);
    }

    @EventHandler
    public void stopPickaxePickup(@Nonnull final PlayerAttemptPickupItemEvent e) {
        final Player p = e.getPlayer();
        final ItemStack item = e.getItem().getItemStack();
        if (Permission.DISPOSE_OWN_PICKAXE.has(p, this.core)) return;
        if (isPickaxe(item)) e.setCancelled(true);
    }

    @EventHandler
    public void stopPickaxeDropOnDeath(@Nonnull final PlayerDeathEvent e) {
        final Iterator<ItemStack> iterator = e.getDrops().iterator();
        while (iterator.hasNext()) {
            final ItemStack droppedItem = iterator.next();
            if (isPickaxe(droppedItem)) {
                this.playerPickaxesWhoDied.putIfAbsent(e.getEntity(), droppedItem);
                iterator.remove();
            }
        }
    }


    @EventHandler
    public void stopPickaxeDrop(@Nonnull final PlayerDropItemEvent e) {
        final Player p = e.getPlayer();
        final ItemStack droppedItem = e.getItemDrop().getItemStack();
        if (isPickaxe(droppedItem)) {
            if (Permission.DISPOSE_OWN_PICKAXE.has(p, this.core)) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void stopPickaxeChestTransfer(@Nonnull final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        final ItemStack item = e.getCurrentItem();
        if (isPickaxe(item)) {
            if (Permission.DISPOSE_OWN_PICKAXE.has(p, this.core)) return;
            e.setCancelled(true);
        }
    }
}
