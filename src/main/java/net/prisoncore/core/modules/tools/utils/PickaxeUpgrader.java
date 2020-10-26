package net.prisoncore.core.modules.tools.utils;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.tools.pickaxe.SuperPickaxe;
import net.prisoncore.core.utils.dataservice.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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

public final class PickaxeUpgrader {

    private final PrisonCore core;
    private final PickaxeSettings settings;

    public PickaxeUpgrader(@Nonnull final PrisonCore core,
                           @Nonnull final PickaxeSettings settings) {
        this.core = core;
        this.settings = settings;
    }

    /**
     * Upgrades Efficiency level on a pickaxe and
     * removes relative tokens for upgrade cost
     * @param pickaxe SuperPickaxe to upgrade
     * @param p Player to use
     * @param amount Amount of levels to use
     */
    public void upgradeEfficiency(@Nonnull final SuperPickaxe pickaxe,
                                   @Nonnull final Player p, @Nonnull final Inventory clickedInventory,
                                   int amount) {
        final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());
        final int currentLevel = pickaxe.getEnchantmentLevel(Enchantment.DIG_SPEED);
        if (currentLevel >= this.settings.getMaxEfficiencyLevel()) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cThis enchant is already at max level!"));
            return;
        }

        if ((currentLevel + amount) > this.settings.getMaxEfficiencyLevel()) amount = this.settings.getMaxEfficiencyLevel() - currentLevel;

        final int cost = getUpgradeCost(Enchantment.DIG_SPEED, currentLevel, amount);
        if (user.getTokens() < cost) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cYou do not have enough tokens to upgrade this enchant!"));
            return;
        }

        user.removeTokens(cost);
        pickaxe.addEnchantmentLevel(Enchantment.DIG_SPEED, amount);
        p.sendMessage(textOfRaw("&e&l+" + amount + " &3&l&oEfficiency"));
        playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
        clickedInventory.setItem(4, pickaxe.createPickaxe());
        clickedInventory.setItem(10, this.getEfficiencyItem(pickaxe.getEnchantmentLevel(Enchantment.DIG_SPEED)));
        p.updateInventory();
    }

    /**
     * Upgrades Fortune level on a pickaxe and
     * removes relative tokens for upgrade cost
     * @param pickaxe SuperPickaxe to upgrade
     * @param p Player to use
     * @param amount Amount of levels to use
     */
    public void upgradeFortune(@Nonnull final SuperPickaxe pickaxe,
                                @Nonnull final Player p, @Nonnull final Inventory clickedInventory,
                                int amount) {
        final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());
        final int currentLevel = pickaxe.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        if (currentLevel >= this.settings.getMaxFortuneLevel()) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cThis enchant is already at max level!"));
            return;
        }

        if ((currentLevel + amount) > this.settings.getMaxFortuneLevel()) amount = this.settings.getMaxFortuneLevel() - currentLevel;

        final int cost = getUpgradeCost(Enchantment.LOOT_BONUS_BLOCKS, currentLevel, amount);
        if (user.getTokens() < cost) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cYou do not have enough tokens to upgrade this enchant!"));
            return;
        }

        user.removeTokens(cost);
        pickaxe.addEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS, amount);
        p.sendMessage(textOfRaw("&e&l+" + amount + " &3&l&oFortune"));
        playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
        clickedInventory.setItem(4, pickaxe.createPickaxe());
        clickedInventory.setItem(11, this.getFortuneItem(pickaxe.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)));
        p.updateInventory();
    }

    /**
     * Upgrades night vision level on a pickaxe and
     * removes relative tokens for upgrade cost
     * @param pickaxe SuperPickaxe to upgrade
     * @param p Player to use
     * @param amount Amount of levels to use
     */
    public void upgradeNightVision(@Nonnull final SuperPickaxe pickaxe,
                                    @Nonnull final Player p, @Nonnull final Inventory clickedInventory,
                                    int amount) {
        final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());
        final int currentLevel = pickaxe.getEnchantmentLevel(PickaxeEnchant.NIGHT_VISION);
        if (currentLevel >= this.settings.getMaxNightVisionLevel()) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cThis enchant is already at max level!"));
            return;
        }

        if ((currentLevel + amount) > this.settings.getMaxNightVisionLevel()) amount = this.settings.getMaxNightVisionLevel() - currentLevel;

        final int cost = getUpgradeCost(PickaxeEnchant.NIGHT_VISION, currentLevel, amount);
        if (user.getTokens() < cost) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cYou do not have enough tokens to upgrade this enchant!"));
            return;
        }

        user.removeTokens(cost);
        pickaxe.addEnchantmentLevel(PickaxeEnchant.NIGHT_VISION, amount);
        p.sendMessage(textOfRaw("&e&l+" + amount + " &3&l&oNight Vision"));
        playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
        clickedInventory.setItem(4, pickaxe.createPickaxe());
        clickedInventory.setItem(12, this.getNightVisionItem(pickaxe.getEnchantmentLevel(PickaxeEnchant.NIGHT_VISION)));
        p.updateInventory();
    }

    /**
     * Upgrades Speed level on a pickaxe and
     * removes relative tokens for upgrade cost
     * @param pickaxe SuperPickaxe to upgrade
     * @param p Player to use
     * @param amount Amount of levels to use
     */
    public void upgradeSpeed(@Nonnull final SuperPickaxe pickaxe,
                              @Nonnull final Player p, @Nonnull final Inventory clickedInventory,
                              int amount) {
        final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());
        final int currentLevel = pickaxe.getEnchantmentLevel(PickaxeEnchant.SPEED);
        if (currentLevel >= this.settings.getMaxSpeedLevel()) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cThis enchant is already at max level!"));
            return;
        }

        if ((currentLevel + amount) > this.settings.getMaxSpeedLevel()) amount = this.settings.getMaxSpeedLevel() - currentLevel;

        final int cost = getUpgradeCost(PickaxeEnchant.SPEED, currentLevel, amount);
        if (user.getTokens() < cost) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cYou do not have enough tokens to upgrade this enchant!"));
            return;
        }

        user.removeTokens(cost);
        pickaxe.addEnchantmentLevel(PickaxeEnchant.SPEED, amount);
        p.sendMessage(textOfRaw("&e&l+" + amount + " &3&l&oSpeed"));
        playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
        clickedInventory.setItem(4, pickaxe.createPickaxe());
        clickedInventory.setItem(13, this.getSpeedItem(pickaxe.getEnchantmentLevel(PickaxeEnchant.SPEED)));
        p.updateInventory();
    }

    /**
     * Upgrades Jump level on a pickaxe and
     * removes relative tokens for upgrade cost
     * @param pickaxe SuperPickaxe to upgrade
     * @param p Player to use
     * @param amount Amount of levels to use
     */
    public void upgradeJump(@Nonnull final SuperPickaxe pickaxe,
                             @Nonnull final Player p, @Nonnull final Inventory clickedInventory,
                             int amount) {
        final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());
        final int currentLevel = pickaxe.getEnchantmentLevel(PickaxeEnchant.JUMP);
        if (currentLevel >= this.settings.getMaxJumpLevel()) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cThis enchant is already at max level!"));
            return;
        }

        if ((currentLevel + amount) > this.settings.getMaxJumpLevel()) amount = this.settings.getMaxJumpLevel() - currentLevel;

        final int cost = getUpgradeCost(PickaxeEnchant.JUMP, currentLevel, amount);
        if (user.getTokens() < cost) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cYou do not have enough tokens to upgrade this enchant!"));
            return;
        }

        user.removeTokens(cost);
        pickaxe.addEnchantmentLevel(PickaxeEnchant.JUMP, amount);
        p.sendMessage(textOfRaw("&e&l+" + amount + " &3&l&oJump"));
        playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
        clickedInventory.setItem(4, pickaxe.createPickaxe());
        clickedInventory.setItem(14, this.getJumpItem(pickaxe.getEnchantmentLevel(PickaxeEnchant.JUMP)));
        p.updateInventory();
    }

    /**
     * Upgrades Haste level on a pickaxe and
     * removes relative tokens for upgrade cost
     * @param pickaxe SuperPickaxe to upgrade
     * @param p Player to use
     * @param amount Amount of levels to use
     */
    public void upgradeHaste(@Nonnull final SuperPickaxe pickaxe,
                              @Nonnull final Player p, @Nonnull final Inventory clickedInventory,
                              int amount) {
        final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());
        final int currentLevel = pickaxe.getEnchantmentLevel(PickaxeEnchant.HASTE);
        if (currentLevel >= this.settings.getMaxHasteLevel()) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cThis enchant is already at max level!"));
            return;
        }

        if ((currentLevel + amount) > this.settings.getMaxHasteLevel()) amount = this.settings.getMaxHasteLevel() - currentLevel;

        final int cost = getUpgradeCost(PickaxeEnchant.HASTE, currentLevel, amount);
        if (user.getTokens() < cost) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cYou do not have enough tokens to upgrade this enchant!"));
            return;
        }

        user.removeTokens(cost);
        pickaxe.addEnchantmentLevel(PickaxeEnchant.HASTE, amount);
        p.sendMessage(textOfRaw("&e&l+" + amount + " &3&l&oHaste"));
        playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
        clickedInventory.setItem(4, pickaxe.createPickaxe());
        clickedInventory.setItem(15, this.getHasteItem(pickaxe.getEnchantmentLevel(PickaxeEnchant.HASTE)));
        p.updateInventory();
    }

    /**
     * Upgrades TokenFinder level on a pickaxe and
     * removes relative tokens for upgrade cost
     * @param pickaxe SuperPickaxe to upgrade
     * @param p Player to use
     * @param amount Amount of levels to use
     */
    public void upgradeTokenFinder(@Nonnull final SuperPickaxe pickaxe,
                                    @Nonnull final Player p, @Nonnull final Inventory clickedInventory,
                                    int amount) {
        final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());
        final int currentLevel = pickaxe.getEnchantmentLevel(PickaxeEnchant.TOKEN_FINDER);
        if (currentLevel >= this.settings.getMaxTokenFinderLevel()) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cThis enchant is already at max level!"));
            return;
        }

        if ((currentLevel + amount) > this.settings.getMaxTokenFinderLevel()) amount = this.settings.getMaxTokenFinderLevel() - currentLevel;

        final int cost = getUpgradeCost(PickaxeEnchant.TOKEN_FINDER, currentLevel, amount);
        if (user.getTokens() < cost) {
            playSound(p, Sound.ENTITY_VILLAGER_NO);
            p.sendMessage(textOf("&cYou do not have enough tokens to upgrade this enchant!"));
            return;
        }

        user.removeTokens(cost);
        pickaxe.addEnchantmentLevel(PickaxeEnchant.TOKEN_FINDER, amount);
        p.sendMessage(textOfRaw("&e&l+" + amount + " &3&l&oToken Finder"));
        playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
        clickedInventory.setItem(4, pickaxe.createPickaxe());
        clickedInventory.setItem(19, this.getTokenFinderItem(pickaxe.getEnchantmentLevel(PickaxeEnchant.TOKEN_FINDER)));
        p.updateInventory();
    }

    public final Inventory getPickaxeUpgradeInventory(@Nonnull final Player player) {
        final User user = this.core.getPlayerDataService().getUser(player.getUniqueId());
        final SuperPickaxe pickaxe = user.getPickaxe();

        final int efficiencyLevel = pickaxe.getEnchantmentLevel(Enchantment.DIG_SPEED);
        final int fortuneLevel = pickaxe.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        final int nightVisionLevel = pickaxe.getEnchantmentLevel(PickaxeEnchant.NIGHT_VISION);
        final int speedLevel = pickaxe.getEnchantmentLevel(PickaxeEnchant.SPEED);
        final int jumpLevel = pickaxe.getEnchantmentLevel(PickaxeEnchant.JUMP);
        final int hasteLevel = pickaxe.getEnchantmentLevel(PickaxeEnchant.HASTE);
        final int tokenFinderLevel = pickaxe.getEnchantmentLevel(PickaxeEnchant.TOKEN_FINDER);

        final Inventory inv = Bukkit.createInventory(new PickaxeUpgradeHolder(), 54, textOfRaw("     &b&lUpgrade Your Pickaxe!"));
        final ItemStack border = this.getBorder();

        for (int x = 0; x <= 8; x++) {
            inv.setItem(x, border);
        }
        inv.setItem(4, user.getPickaxe().createPickaxe());
        inv.setItem(9, border);
        inv.setItem(10, this.getEfficiencyItem(efficiencyLevel));
        inv.setItem(11, this.getFortuneItem(fortuneLevel));
        inv.setItem(12, this.getNightVisionItem(nightVisionLevel));
        inv.setItem(13, this.getSpeedItem(speedLevel));
        inv.setItem(14, this.getJumpItem(jumpLevel));
        inv.setItem(15, this.getHasteItem(hasteLevel));
        inv.setItem(17, border);
        inv.setItem(18, border);
        inv.setItem(19, this.getTokenFinderItem(tokenFinderLevel));
        inv.setItem(26, border);
        return inv;
    }

    private ItemStack getBorder() {
        final ItemStack border = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1);
        final ItemMeta meta = border.getItemMeta();
        meta.setDisplayName(textOfRaw("&f "));
        border.setItemMeta(meta);
        return border;
    }

    private ItemStack getEfficiencyItem(final int currentLevel) {
        final ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK, 1);
        final ItemMeta meta = stack.getItemMeta();
        final int cost = getUpgradeCost(Enchantment.DIG_SPEED, currentLevel, 1);
        final int costPer10 = getUpgradeCost(Enchantment.DIG_SPEED, currentLevel, 10);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(textOfRaw("&3&l&o       EFFICIENCY"));
        final List<String> lore = new ArrayList<>();
        this.addCostToLore(lore, cost, costPer10, currentLevel, this.settings.getMaxEfficiencyLevel());
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getFortuneItem(final int currentLevel) {
        final ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK, 1);
        final ItemMeta meta = stack.getItemMeta();
        final int cost = getUpgradeCost(Enchantment.LOOT_BONUS_BLOCKS, currentLevel, 1);
        final int costPer10 = getUpgradeCost(Enchantment.LOOT_BONUS_BLOCKS, currentLevel, 10);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(textOfRaw("&3&l&o          FORTUNE"));
        final List<String> lore = new ArrayList<>();
        this.addCostToLore(lore, cost, costPer10, currentLevel, this.settings.getMaxFortuneLevel());
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getNightVisionItem(final int currentLevel) {
        final ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK, 1);
        final ItemMeta meta = stack.getItemMeta();
        final int cost = getUpgradeCost(PickaxeEnchant.NIGHT_VISION, currentLevel, 1);
        final int costPer10 = getUpgradeCost(PickaxeEnchant.NIGHT_VISION, currentLevel, 10);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(textOfRaw("&3&l&o      NIGHT VISION"));
        final List<String> lore = new ArrayList<>();
        this.addCostToLore(lore, cost, costPer10, currentLevel, this.settings.getMaxNightVisionLevel());
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getSpeedItem(final int currentLevel) {
        final ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK, 1);
        final ItemMeta meta = stack.getItemMeta();
        final int cost = getUpgradeCost(PickaxeEnchant.SPEED, currentLevel, 1);
        final int costPer10 = getUpgradeCost(PickaxeEnchant.SPEED, currentLevel, 10);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(textOfRaw("&3&l&o            SPEED"));
        final List<String> lore = new ArrayList<>();
        this.addCostToLore(lore, cost, costPer10, currentLevel, this.settings.getMaxSpeedLevel());
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getJumpItem(final int currentLevel) {
        final ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK, 1);
        final ItemMeta meta = stack.getItemMeta();
        final int cost = getUpgradeCost(PickaxeEnchant.JUMP, currentLevel, 1);
        final int costPer10 = getUpgradeCost(PickaxeEnchant.JUMP, currentLevel, 10);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(textOfRaw("&3&l&o          JUMP"));
        final List<String> lore = new ArrayList<>();
        this.addCostToLore(lore, cost, costPer10, currentLevel, this.settings.getMaxJumpLevel());
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getHasteItem(final int currentLevel) {
        final ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK, 1);
        final ItemMeta meta = stack.getItemMeta();
        final int cost = getUpgradeCost(PickaxeEnchant.HASTE, currentLevel, 1);
        final int costPer10 = getUpgradeCost(PickaxeEnchant.HASTE, currentLevel, 10);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(textOfRaw("&3&l&o          HASTE"));
        final List<String> lore = new ArrayList<>();
        this.addCostToLore(lore, cost, costPer10, currentLevel, this.settings.getMaxHasteLevel());
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }


    private ItemStack getTokenFinderItem(final int currentLevel) {
        final ItemStack stack = new ItemStack(Material.ENCHANTED_BOOK, 1);
        final ItemMeta meta = stack.getItemMeta();
        final int cost = getUpgradeCost(PickaxeEnchant.TOKEN_FINDER, currentLevel, 1);
        final int costPer10 = getUpgradeCost(PickaxeEnchant.TOKEN_FINDER, currentLevel, 10);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(textOfRaw("&e&l&o       TOKEN FINDER"));
        final List<String> lore = new ArrayList<>();
        this.addCostToLore(lore, cost, costPer10, currentLevel, this.settings.getMaxTokenFinderLevel());
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public final int getUpgradeCost(@Nonnull final Enchantment enchant,
                                    int currentLevel, final int levelsToIncrease) {
        int total = 0;
        for (int x = 1; x <= levelsToIncrease;) {
            if (enchant == Enchantment.DIG_SPEED) {
                total += this.getEfficiencyUpgradeCost(currentLevel);
            } else if (enchant == Enchantment.LOOT_BONUS_BLOCKS) {
                total += this.getFortuneUpgradeCost(currentLevel);
            }
            currentLevel++;
            x++;
        }
        return total;
    }

    public final int getUpgradeCost(@Nonnull final PickaxeEnchant enchant,
                                    int currentLevel, final int levelsToIncrease) {
        int total = 0;
        for (int x = 1; x <= levelsToIncrease;) {

            switch (enchant) {
                case JUMP:
                    total += this.getJumpUpgradeCost(currentLevel);
                    break;
                case HASTE:
                    total += this.getHasteUpgradeCost(currentLevel);
                    break;
                case SPEED:
                    total += this.getSpeedUpgradeCost(currentLevel);
                    break;
                case NIGHT_VISION:
                    total += this.getNightVisionUpgradeCost(currentLevel);
                    break;
                case TOKEN_FINDER:
                    total += getTokenFinderUpgradeCost(currentLevel);
                    break;
            }
            currentLevel++;
            x++;
        }
        return total;
    }

    private void addCostToLore(@Nonnull final List<String> lore, final int cost, final int costPer10,
                               final int currentLevel, final int maxLevel) {
        lore.add(textOfRaw("&f "));
        lore.add(textOfRaw("&b&l| &e&oUpgrade Cost &7(1) &f&l" + cost + "&6 Tokens"));
        lore.add(textOfRaw("&b&l| &e&oUpgrade Cost &7(10) &f&l" + costPer10 + "&6 Tokens"));
        lore.add(textOfRaw("&b&l| &3&oCurrent Level &d&l" + currentLevel));
        lore.add(textOfRaw("&b&l| &3&oMaximum Level &d&l" + maxLevel));
        lore.add(textOfRaw("&f "));
        lore.add(textOfRaw("&e&oClick to upgrade!"));
    }

    public final double getEfficiencyUpgradeCost(final int efficiencyLevel) {
        return (this.settings.getEfficiencyCostIncrease() * efficiencyLevel) + this.settings.getEfficiencyBaseCost();
    }

    public final double getFortuneUpgradeCost(final int fortuneLevel) {
        return (this.settings.getFortuneCostIncrease() * fortuneLevel) + this.settings.getFortuneBaseCost();
    }

    public final double getNightVisionUpgradeCost(final int nightVisionLevel) {
        return (this.settings.getNightVisionCostIncrease() * nightVisionLevel) + this.settings.getNightVisionBaseCost();
    }

    public final double getSpeedUpgradeCost(final int speedLevel) {
        return (this.settings.getSpeedCostIncrease() * speedLevel) + this.settings.getSpeedBaseCost();
    }

    public final double getJumpUpgradeCost(final int jumpLevel) {
        return (this.settings.getJumpCostIncrease() * jumpLevel) + this.settings.getJumpBaseCost();
    }

    public final double getHasteUpgradeCost(final int hasteLevel) {
        return (this.settings.getHasteCostIncrease() * hasteLevel) + this.settings.getHasteBaseCost();
    }

    public final double getTokenFinderUpgradeCost(final int tokenFinderLevel) {
        return (this.settings.getTokenFinderCostIncrease() * tokenFinderLevel) + this.settings.getTokenFinderBaseCost();
    }
}
