package net.prisoncore.core.modules.prisonmmo.modules.mining.events;

import net.prisoncore.core.modules.prisonmmo.modules.mining.MiningMMOModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

public final class BlockBreak implements Listener {

    private final MiningMMOModule module;

    public BlockBreak(@Nonnull final MiningMMOModule module) {
        this.module = module;
    }

    @EventHandler
    public void onBreak(@Nonnull final BlockBreakEvent e) {
        this.handleExp(e.getBlock().getType(), e.getPlayer().getUniqueId());
    }

    private void handleExp(@Nonnull final Material broken, @Nonnull final UUID uuid) {
        final double amt = this.module.getExpForMaterial(broken);
        if (amt < 0D) return;
        this.module.addExp(uuid, amt);
    }

    private void handleDoubleDrops(@Nonnull final Material mat) {

    }
}
