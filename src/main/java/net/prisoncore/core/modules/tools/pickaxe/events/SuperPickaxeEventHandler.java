package net.prisoncore.core.modules.tools.pickaxe.events;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.tools.pickaxe.SuperPickaxe;
import net.prisoncore.core.modules.tools.utils.PickaxeSettings;
import net.prisoncore.core.utils.dataservice.User;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.UUID;

import static net.prisoncore.core.utils.Utils.isPickaxe;

public final class SuperPickaxeEventHandler implements Listener {

    private final PrisonCore core;
    private final PickaxeSettings settings;
    private final HashMap<UUID, Integer> pickaxeRefreshRate;

    public SuperPickaxeEventHandler(@Nonnull final PrisonCore core,
                                    @Nonnull final PickaxeSettings settings) {
        this.core = core;
        this.settings = settings;
        this.pickaxeRefreshRate = new HashMap<>();
    }


    @EventHandler // This event ensures player's always have they're pickaxe when they join
    public void onPlayerJoin(@Nonnull final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());

        if (!isPickaxe(p.getInventory().getItem(0))) {
            p.getInventory().setItem(0, user.getPickaxe().createPickaxe());
        }
    }

    @EventHandler // This handles most custom pickaxe enchantments
    public void onBlockBreak(@Nonnull final BlockBreakEvent e) {
        if (isPickaxe(e.getPlayer().getInventory().getItemInMainHand())) {
            final User user = this.core.getPlayerDataService().getUser(e.getPlayer().getUniqueId());
            final Block brokenBlock = e.getBlock();
            this.handlePickaxeExp(user, brokenBlock);
            this.handleTokenFinder(user, brokenBlock);

            this.handlePickaxeRefresh(user);
        }
    }

    /**
     * Handles the giving and leveling up of a Player's SuperPickaxe
     * @param user effected user
     * @param block Block broken
     */
    private void handlePickaxeExp(@Nonnull final User user, @Nonnull final Block block) {
        if (this.settings.getExpForBlock(block) == 0D) return;
        final double baseExp = this.settings.getExpForBlock(block);
        if (baseExp == 0D) return;
        final SuperPickaxe pickaxe = user.getPickaxe();

        pickaxe.addExp(baseExp);
    }

    /**
     * Handles the giving of tokens when a user's pickaxe has Token Miner
     * @param user effected user
     * @param block block that was broken by user
     */
    private void handleTokenFinder(@Nonnull final User user, @Nonnull final Block block) {
        if (this.settings.getTokenFinderAmount(block) == 0D) return;
        final double baseTokens = this.settings.getTokenFinderAmount(block);
        if (baseTokens == 0D) return;
        user.addTokens(baseTokens);
    }

    /**
     * This handles refreshing a User's pickaxe
     * to reduce lag we only update the player's pickaxe after they have
     * mined a certain amount of blocks
     * @param user effected user
     */
    private void handlePickaxeRefresh(@Nonnull final User user) {
        if (this.pickaxeRefreshRate.get(user.getUuid()) == null) {
            this.pickaxeRefreshRate.putIfAbsent(user.getUuid(), 1);
        } else {
            int amtBroken = this.pickaxeRefreshRate.get(user.getUuid());
            if (amtBroken >= this.settings.getRefreshRate()) {
                final Player p = user.getPlayer();
                p.getInventory().setItemInMainHand(user.getPickaxe().createPickaxe());
                this.pickaxeRefreshRate.replace(user.getUuid(), 1);
            } else {
                amtBroken += 1;
                this.pickaxeRefreshRate.replace(user.getUuid(), amtBroken);
            }
        }
    }
}
