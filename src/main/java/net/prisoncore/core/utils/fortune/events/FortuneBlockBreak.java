package net.prisoncore.core.utils.fortune.events;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.shop.autosell.AutoSell;
import net.prisoncore.core.modules.tools.pickaxe.SuperPickaxe;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.fortune.FortuneModule;
import net.prisoncore.core.utils.fortune.utils.AutoSellEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collection;

import static net.prisoncore.core.utils.Utils.isPickaxe;

/**
 * This class is an event handler to customize fortune on ALL of our blocks on the server,
 * it first checks if the item in the BlockBreaker's hand is infact a SuperPickaxe (eg just a pickaxe)
 * after that it checks the fortune level, and applies it promptly to all blocks on the server
 *
 * POSSIBLE KNOWN BUGS: If a item has multiple drops, it will make it randomly select one of those
 * drops and apply fortune to it
 */
public final class FortuneBlockBreak implements Listener {

    private final FortuneModule module;
    private final PrisonCore core;

    public FortuneBlockBreak(@Nonnull final FortuneModule module) {
        this.module = module;
        this.core = this.module.getCore();
    }

    @EventHandler
    public void onPrisonBlockBreak(@Nonnull final AutoSellEvent e) {
        Bukkit.broadcastMessage("Dang this actually worked lol");
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onBreak(@Nonnull final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        final ItemStack pickaxeItem = e.getPlayer().getInventory().getItemInMainHand();
        final Material brokenMaterial = e.getBlock().getType();
        if (this.module.isBlacklisted(brokenMaterial)) {
            // TODO: Maybe add a message here?
            return;
        }
        if (!isPickaxe(pickaxeItem)) return;

        final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());
        final SuperPickaxe pickaxe = user.getPickaxe();

        final Material droppedMaterial = this.getMainBrokenMaterial(e.getBlock().getDrops(pickaxeItem));
        if (droppedMaterial == null) return;
        final int fortuneAmount = pickaxe.getFortuneDropsAmount();
        final Collection<ItemStack> drops = this.getFortuneDrops(droppedMaterial, fortuneAmount);
        e.setDropItems(false);
        e.setExpToDrop(0); // TODO: We will make a custom EXP module eventually

        if (user.isAutoSellEnabled()) {
            this.processAutoSellEvent(user, droppedMaterial, fortuneAmount);
        } else {
            drops.forEach((drop) -> p.getInventory().addItem(drop));
        }
    }

    /**
     * Process's and calls a AutoSellEvent for specified User
     * @param user trigger of the event
     * @param material dropped material
     * @param fortuneAmount amount dropped
     */
    private void processAutoSellEvent(@Nonnull final User user,
                                      @Nonnull final Material material,
                                      final int fortuneAmount) {
        final AutoSellEvent event = new AutoSellEvent(user.getPlayer(), this.core, material, fortuneAmount);
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Gets the first broken block type of the collection provided
     * @param col Collection to use
     * @return null if the Collection yields no ItemStacks
     */
    private Material getMainBrokenMaterial(final Collection<ItemStack> col) {
        if (col == null) return null;
        if (col.isEmpty()) return null;
        final ItemStack stack = ((ItemStack) col.toArray()[0]);
        if (stack == null) return null;
        return stack.getType();
    }

    private Collection<ItemStack> getFortuneDrops(@Nonnull final Material mat, int fortuneAmount) {
        final Collection<ItemStack> col = new ArrayList<>();
        if (fortuneAmount >= 64) {
            col.add(new ItemStack(mat, fortuneAmount));
        } else {
            final int stackAmount = (fortuneAmount / 64);
            final int leftover = fortuneAmount - (stackAmount * 64);
            for (int x = 0; x < stackAmount; x++) {
                col.add(new ItemStack(mat, 64));
            }
            col.add(new ItemStack(mat, leftover));
        }
        return col;
    }
}
