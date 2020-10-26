package net.prisoncore.core.modules.essentials.clearinventory;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.essentials.clearinventory.commands.ClearInventoryCommand;
import net.prisoncore.core.modules.essentials.clearinventory.events.ConfirmClearRequest;
import net.prisoncore.core.modules.tools.pickaxe.SuperPickaxe;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.messages.Message;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;

import javax.annotation.Nonnull;

import static net.prisoncore.core.utils.Utils.playSound;

public final class ClearInventoryModule extends Module {

    private final PrisonCore core;

    private ConfirmClearRequest confirmClearRequest;

    public ClearInventoryModule(@Nonnull final PrisonCore core) {
        this.core = core;
    }


    @Override
    public Module init() {
        this.core.getLog().log("&3Initializing ClearInventoryModule. . . ");
        this.core.registerCommand("clearinventory", new ClearInventoryCommand(this.core, this));

        this.confirmClearRequest = new ConfirmClearRequest(this.core);
        this.core.registerEvent(this.confirmClearRequest);
        this.core.getLog().log("&3ClearInventoryModule initialized!");
        return this;
    }

    @Override
    public void reload() {
        HandlerList.unregisterAll(this.confirmClearRequest);
        this.confirmClearRequest = new ConfirmClearRequest(this.core);
        this.core.registerEvent(this.confirmClearRequest);
    }

    public void clearInventory(@Nonnull final User user) {
        final Player p = user.getPlayer();
        final SuperPickaxe pickaxe = user.getPickaxe();
        p.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        p.getInventory().clear();
        p.getInventory().setItem(0, pickaxe.createPickaxe());
        playSound(p, Sound.ENTITY_ITEM_BREAK);
        Message.TARGET_INVENTORY_CLEARED.sendMessage(p, this.core);
    }
}
