package net.prisoncore.core.utils.fortune.utils;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.utils.dataservice.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public final class AutoSellEvent extends Event implements Cancellable {

    private static final HandlerList list = new HandlerList();
    private final Player breaker;
    private final PrisonCore core;
    private final Material brokenMaterial;
    private final int amount;

    private boolean cancelled = false;

    public AutoSellEvent(@Nonnull final Player player,
                         @Nonnull final PrisonCore core,
                         @Nonnull final Material materialBroken,
                         final int amountBroken) {
        this.breaker = player;
        this.core = core;
        this.brokenMaterial = materialBroken;
        this.amount = amountBroken;
    }

    /**
     * @return the User object of the Player who broke the block
     */
    public final User getUser() {
        return this.core.getPlayerDataService().getUser(this.breaker.getUniqueId());
    }

    /**
     * @return Material that was broken
     */
    public final Material getBrokenMaterial() {
        return this.brokenMaterial;
    }

    /**
     * @return amount of material broken
     */
    public final int getAmountBroken() {
        return this.amount;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}
