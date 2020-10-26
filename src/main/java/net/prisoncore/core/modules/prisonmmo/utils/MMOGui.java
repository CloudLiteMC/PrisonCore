package net.prisoncore.core.modules.prisonmmo.utils;

import net.prisoncore.core.modules.prisonmmo.PrisonMMOModule;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.dataservice.UserMMO;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import static net.prisoncore.core.utils.Utils.textOfRaw;

public class MMOGui {

    private final PrisonMMOModule module;

    public MMOGui(@Nonnull final PrisonMMOModule module) {
        this.module = module;
    }

    public final Inventory getMainPage(@Nonnull final User user) {
        final UserMMO mmo = user.getMMO();
        mmo.setPlayerExpIncrease(this.module.getPlayerExpIncrease());
        mmo.setMiningExpIncrease(this.module.getMiningExpIncrease());
        final Inventory inv = Bukkit.createInventory(new MMOGuiHolder(), 27, textOfRaw("&3&lYOUR MMO SKILLS"));
        final ItemStack border = this.getBorder();

        for (int x = 0; x <= 8; x++) {
            inv.setItem(x, border);
        }

        inv.setItem(4, this.createMainPageItem(Material.SUNFLOWER,
                "Player",
                mmo.getPlayerLevel(),
                this.module.getPlayerExpNeeded(mmo)));
        inv.setItem(9, border);
        inv.setItem(10, border);
        inv.setItem(11, this.createMainPageItem(Material.NETHERITE_PICKAXE,
                "Mining",
                mmo.getMiningLevel(),
                this.module.getMiningExpNeeded(mmo)));
        inv.setItem(16, border);
        inv.setItem(17, border);
        for (int x = 18; x <= 26; x++) {
            inv.setItem(x, border);
        }
        return inv;
    }

    private ItemStack createMainPageItem(@Nonnull final Material material,
                                         @Nonnull final String mmoSkill,
                                         final int mmoLevel,
                                         final double expNeeded) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(textOfRaw("&e&l&o" + mmoSkill));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        final List<String> lore = new ArrayList<>();
        lore.add(textOfRaw("&f "));
        lore.add(textOfRaw("&3Level &e" + mmoLevel));
        lore.add(textOfRaw("&3EXP Until next level &e" + expNeeded));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getBorder() {
        final ItemStack item = new ItemStack(Material.CYAN_STAINED_GLASS_PANE, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(textOfRaw(textOfRaw("&f ")));
        item.setItemMeta(meta);
        return item;
    }
}
