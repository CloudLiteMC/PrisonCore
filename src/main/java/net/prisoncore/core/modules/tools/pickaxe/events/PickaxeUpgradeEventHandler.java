package net.prisoncore.core.modules.tools.pickaxe.events;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.tools.pickaxe.SuperPickaxe;
import net.prisoncore.core.modules.tools.utils.PickaxeSettings;
import net.prisoncore.core.modules.tools.utils.PickaxeUpgradeHolder;
import net.prisoncore.core.modules.tools.utils.PickaxeUpgrader;
import net.prisoncore.core.utils.dataservice.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

import static net.prisoncore.core.utils.Utils.isPickaxe;

public final class PickaxeUpgradeEventHandler implements Listener {

    private final PrisonCore core;
    private final PickaxeUpgrader upgrader;

    public PickaxeUpgradeEventHandler(@Nonnull final PrisonCore core,
                                      @Nonnull final PickaxeSettings settings) {
        this.core = core;
        this.upgrader = new PickaxeUpgrader(this.core, settings);
    }

    @EventHandler
    public void onRightClick(@Nonnull final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        final Action action = e.getAction();
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK) && isPickaxe(p.getInventory().getItemInMainHand())) {
            final Inventory inv = this.upgrader.getPickaxeUpgradeInventory(p);
            p.openInventory(inv);
        }
    }

    @EventHandler
    public void onInventoryClick(@Nonnull final InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof PickaxeUpgradeHolder) {
            e.setCancelled(true);
            final Inventory clickedInventory = e.getInventory();
            final int slotClicked = e.getSlot();
            final Player p = (Player) e.getWhoClicked();
            final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());
            final SuperPickaxe pickaxe = user.getPickaxe();
            final ClickType type = e.getClick();
            if (slotClicked == 10) { // Efficiency
                if (type.isLeftClick()) {
                    this.upgrader.upgradeEfficiency(pickaxe, p, clickedInventory, 1);
                } else if (type.isRightClick()) {
                    this.upgrader.upgradeEfficiency(pickaxe, p, clickedInventory, 10);
                }
            } else if (slotClicked == 11) { // Fortune
                if (type.isLeftClick()) {
                    this.upgrader.upgradeFortune(pickaxe, p, clickedInventory, 1);
                } else if (type.isRightClick()) {
                    this.upgrader.upgradeFortune(pickaxe, p, clickedInventory, 10);
                }
            } else if (slotClicked == 12) { // Night Vision
                if (type.isLeftClick()) {
                    this.upgrader.upgradeNightVision(pickaxe, p, clickedInventory, 1);
                } else if (type.isRightClick()) {
                    this.upgrader.upgradeNightVision(pickaxe, p, clickedInventory, 10);
                }
            } else if (slotClicked == 13) { // Speed
                if (type.isLeftClick()) {
                    this.upgrader.upgradeSpeed(pickaxe, p, clickedInventory, 1);
                } else if (type.isRightClick()) {
                    this.upgrader.upgradeSpeed(pickaxe, p, clickedInventory, 10);
                }
            } else if (slotClicked == 14) { // Jump
                if (type.isLeftClick()) {
                    this.upgrader.upgradeJump(pickaxe, p, clickedInventory, 1);
                } else if (type.isRightClick()) {
                    this.upgrader.upgradeJump(pickaxe, p, clickedInventory, 10);
                }
            } else if (slotClicked == 15) { // Haste
                if (type.isLeftClick()) {
                    this.upgrader.upgradeHaste(pickaxe, p, clickedInventory, 1);
                } else if (type.isRightClick()) {
                    this.upgrader.upgradeHaste(pickaxe, p, clickedInventory, 10);
                }
            } else if (slotClicked == 19) { // Token Finder
                if (type.isLeftClick()) {
                    this.upgrader.upgradeTokenFinder(pickaxe, p, clickedInventory, 1);
                } else {
                    this.upgrader.upgradeTokenFinder(pickaxe, p, clickedInventory, 10);
                }
            }
        }
    }

    @EventHandler
    public final void onInvClose(@Nonnull final InventoryCloseEvent e) {
        final Inventory closedInv = e.getInventory();
        if (closedInv.getHolder() instanceof PickaxeUpgradeHolder) {
            final User user = this.core.getPlayerDataService().getUser(e.getPlayer().getUniqueId());
            e.getPlayer().getInventory().setItemInMainHand(user.getPickaxe().createPickaxe());
        }
    }

}
