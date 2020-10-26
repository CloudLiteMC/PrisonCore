package net.prisoncore.core.modules.rankup.utils;

import net.luckperms.api.model.group.Group;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.rankup.RankupModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import static net.prisoncore.core.utils.Utils.*;

public final class RankupInventory {

    private final RankupModule module;
    private final PrisonCore core;

    public RankupInventory(@Nonnull final RankupModule module) {
        this.module = module;
        this.core = this.module.getCore();
    }

    public final Inventory getInventory(@Nonnull final Player player) {
        final Group playerGroup = getLuckPermsGroup(player.getUniqueId());
        final Inventory inventory = Bukkit.createInventory(new RankupInventoryHolder(), 54, textOfRaw("&3&lAvailable Ranks"));
        final ItemStack border = this.getBorderItem();

        for (int x = 0; x <= 8; x++) {
            inventory.setItem(x, border);
        }

        /*inventory.setItem(9, border);
        inventory.setItem(17, border);
        inventory.setItem(18, border);
        inventory.setItem(26, border);
        inventory.setItem(27, border);
        inventory.setItem(35, border);
        inventory.setItem(36, border);*/

        for (int x = 45; x <= 53; x++) {
            inventory.setItem(x, border);
        }

        this.module.getAllRanks().forEach((rank) -> inventory.addItem(this.getRankItem(rank, playerGroup)));
        return inventory;
    }

    private ItemStack getBorderItem() {
        final ItemStack stack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        final ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(textOfRaw("&f "));
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getRankItem(@Nonnull final Rank rank,
                                  @Nonnull final Group playerCurrentRank) {

        final String thisRankName = rank.getThisRankName();
        final String nextRankName = rank.getNextRankName();
        final double rankCost = rank.getRankupCost();
        final int nextGroupWeight = getLuckPermsGroupWeight(nextRankName);
        final int thisGroupWeight = playerCurrentRank.getWeight().getAsInt();

        final ItemStack item = new ItemStack(rank.getMaterial(), 1);
        final ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(textOfRaw("&b&l&n" + thisRankName + " &e&l&m-&e&l> &b&l&n" + nextRankName));
        final List<String> lore = new ArrayList<>();
        lore.add(textOfRaw("&f "));
        lore.add(textOfRaw("&e&lCOST &2&l$ &a&l" + rankCost));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        if (thisGroupWeight >= nextGroupWeight) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            lore.add(textOfRaw("&a&l✓ &a&oYou have achieved this rank!\""));
        } else lore.add(textOfRaw("&c&l✗ &c&oYou have not achieved this rank yet!"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }
}
