package net.prisoncore.core.modules.tools.pickaxe;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.ModuleType;
import net.prisoncore.core.modules.tools.ToolsModule;
import net.prisoncore.core.modules.tools.utils.PickaxeEnchant;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.*;

import static net.prisoncore.core.utils.Utils.getPercentOf;
import static net.prisoncore.core.utils.Utils.textOfRaw;

public final class SuperPickaxe {

    private final PrisonCore core;
    private final Random random;
    private final Document databaseObject;
    private String pickaxeName;
    private List<String> pickaxeLore;
    private int pickaxeLevel;
    private double pickaxeExp;
    public Map<Enchantment, Integer> pickaxeEnchantments;
    public Map<PickaxeEnchant, Integer> customPickaxeEnchantments;
    private List<String> pickaxeStoredEnchantments;
    private List<String> pickaxeStoredCustomEnchantments;

    public SuperPickaxe(@Nonnull final Document databasePickaxe, @Nonnull final PrisonCore core) {
        this.databaseObject = databasePickaxe;
        this.core = core;
        this.random = new Random();
        this.pickaxeName = databasePickaxe.getString("name");
        this.pickaxeLore = new ArrayList<>();
        this.pickaxeLevel = databasePickaxe.getInteger("level");
        this.pickaxeExp = databasePickaxe.getDouble("currentExp");
        this.pickaxeStoredEnchantments = (List<String>) databasePickaxe.get("enchantments");
        this.pickaxeStoredCustomEnchantments = (List<String>) databasePickaxe.get("customEnchants");
        pickaxeEnchantments = new HashMap<>();
        customPickaxeEnchantments = new HashMap<>();
        this.convertStoredEnchants();
        this.convertStoredCustomEnchants();
    }

    public double getExpUntilLevelUp() {
        final double increasePerLevel = ((ToolsModule) this.core.getModule(ModuleType.TOOLS)).getPickaxeSettings().getExpIncreasePerLvl();
        return (increasePerLevel * this.getPickaxeLevel()) - this.pickaxeExp;
    }

    public int getPickaxeLevel() {
        final double increasePerLevel = ((ToolsModule) this.core.getModule(ModuleType.TOOLS)).getPickaxeSettings().getExpIncreasePerLvl();
        final int result = ((int) this.pickaxeExp + 1) / ((int) increasePerLevel);
        if (result <= 0) return 1;
        return result;
    }

    public void addExp(final double amount) {
        this.pickaxeExp += amount;
    }

    public final Material getPickaxeMaterial() {
        return Material.getMaterial(this.databaseObject.getString("material"));
    }

    public int getFortuneDropsAmount() {
        final int fortuneLevel = this.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        final int min = getPercentOf(50, fortuneLevel);
        final int max = getPercentOf(95, fortuneLevel);
        return (this.random.nextInt((max - min)) + min);
    }

    public int getEnchantmentLevel(@Nonnull final Enchantment enchant) {
        if (pickaxeEnchantments.get(enchant) == null) return 0;
        return pickaxeEnchantments.get(enchant);
    }

    public int getEnchantmentLevel(@Nonnull final PickaxeEnchant enchant) {
        if (customPickaxeEnchantments.get(enchant) == null) return 0;
        return customPickaxeEnchantments.get(enchant);
    }

    public void addEnchantmentLevel(@Nonnull final Enchantment enchant, final int level) {
        if (pickaxeEnchantments.get(enchant) == null) {
            pickaxeEnchantments.putIfAbsent(enchant, level);
        } else {
            int currentLevel = pickaxeEnchantments.get(enchant);
            currentLevel += level;
            pickaxeEnchantments.replace(enchant, currentLevel);
        }
    }

    public void addEnchantmentLevel(@Nonnull final PickaxeEnchant enchant, final int level) {
        if (this.customPickaxeEnchantments.get(enchant) == null) {
            this.customPickaxeEnchantments.putIfAbsent(enchant, level);
        } else {
            int currentLevel = this.customPickaxeEnchantments.get(enchant);
            currentLevel += level;
            this.customPickaxeEnchantments.replace(enchant, currentLevel);
        }
    }

    /**
     * Converts the Custom Enchantments from the Database Document
     * to actual PickaxeEnchant's and Levels
     */
    private void convertStoredCustomEnchants() {
        for (final String enchantment: this.pickaxeStoredCustomEnchantments) {
            final String[] obj = enchantment.split(":");
            final PickaxeEnchant enchant = PickaxeEnchant.valueOf(obj[0].toUpperCase());
            final int level = Integer.parseInt(obj[1]);
            customPickaxeEnchantments.putIfAbsent(enchant, level);
        }
    }

    /**
     * Converts the Vanilla Enchantments from the Database Document
     * to actual Enchants and Levels
     */
    private void convertStoredEnchants() {
        for (final String enchantment : this.pickaxeStoredEnchantments) {
            final String[] obj = enchantment.split(":");
            final Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(obj[0].toLowerCase()));
            final int level = Integer.parseInt(obj[1]);
            pickaxeEnchantments.putIfAbsent(enchant, level);
        }
    }

    /**
     * Adds the Enchantment's and Custom Enchantments to a ItemMeta's Lore
     * This is meant to be used while the server is running to upgrade existing pickaxes
     * @param meta ItemMeta to effect
     */
    private static void addEnchantsToLoreMeta(@Nonnull final ItemMeta meta,
                                              @Nonnull final SuperPickaxe pickaxe) {
        final List<String> lore = new ArrayList<>();
        lore.add(textOfRaw("&f "));
        lore.add(textOfRaw("&6&l&o    PICKAXE ENCHANTMENT'S"));
        lore.add(textOfRaw("&f "));
        pickaxe.pickaxeEnchantments.forEach((enchant, level) -> {
            if (enchant.equals(Enchantment.DIG_SPEED)) {
                lore.add(textOfRaw("&d&l| &7Efficiency &e" + level));
            } else if (enchant.equals(Enchantment.LOOT_BONUS_BLOCKS)) {
                lore.add(textOfRaw("&b&l| &7Fortune &e" + level));
            }
        });
        pickaxe.customPickaxeEnchantments.forEach((enchant, level) -> {
            switch (enchant) {
                case TOKEN_FINDER:
                    lore.add(textOfRaw("&c&l| &7Token Finder &e" + level));
                    break;
                case NIGHT_VISION:
                    lore.add(textOfRaw("&a&l| &7Night Vision &e" + level));
                    break;
                case SPEED:
                    lore.add(textOfRaw("&3&l| &7Speed &e" + level));
                    break;
                case HASTE:
                    lore.add(textOfRaw("&1&l| &7Haste &e" + level));
                    break;
                case JUMP:
                    lore.add(textOfRaw("&5&l| &7Jump &e" + level));
                    break;
            }
        });
        lore.add(textOfRaw("&f "));
        lore.add(textOfRaw("&b&l&o     PICKAXE LEVEL"));
        lore.add(textOfRaw("&f "));
        lore.add(textOfRaw("&e&l| &3Current Level &c" + pickaxe.getPickaxeLevel()));
        lore.add(textOfRaw("&e&l| &3Exp Until Next Level &e" + pickaxe.getExpUntilLevelUp()));
        meta.setLore(lore);
    }

    /**
     * Creates a pickaxe ItemStack, based on the enchantments and data stored in this class
     * @return SuperPickaxe ItemStack
     */
    public @Nonnull final ItemStack createPickaxe() {
        final ItemStack pickaxeItem = new ItemStack(this.getPickaxeMaterial(), 1);
        final ItemMeta pickaxeMeta = pickaxeItem.getItemMeta();

        final NamespacedKey levelKey = new NamespacedKey(this.core, "level");
        final NamespacedKey expKey = new NamespacedKey(this.core, "currentExp");

        pickaxeMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, this.pickaxeLevel);
        pickaxeMeta.getPersistentDataContainer().set(expKey, PersistentDataType.DOUBLE, this.pickaxeExp);

        pickaxeEnchantments.forEach((enchant, level) ->
                pickaxeMeta.addEnchant(enchant, level, true));

        customPickaxeEnchantments.forEach((enchant, level) -> {
            final NamespacedKey key = new NamespacedKey(this.core, enchant.getEnchantName());
            pickaxeMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);
        });

        pickaxeMeta.setDisplayName(textOfRaw(this.pickaxeName));
        addEnchantsToLoreMeta(pickaxeMeta, this);
        pickaxeMeta.setUnbreakable(true);
        pickaxeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        pickaxeItem.setItemMeta(pickaxeMeta);
        return pickaxeItem;
    }

    /**
     * Creates a Document for saving into MongoDB
     * @return Document to save
     */
    public final Document createDatabaseDocument() {
        final Document document = new Document();
        document.append("name", this.pickaxeName);
        document.append("lore", this.pickaxeLore);
        document.append("level", this.pickaxeLevel);
        document.append("currentExp", this.pickaxeExp);
        final List<String> vanillaEnchants = new ArrayList<>();
        this.pickaxeEnchantments.forEach((enchant, level) -> vanillaEnchants.add(enchant.getKey().getKey().toLowerCase() + ":" + level));
        document.append("enchantments", vanillaEnchants);
        final List<String> customEnchants = new ArrayList<>();
        this.customPickaxeEnchantments.forEach((enchant, level) -> customEnchants.add(enchant.getEnchantName() + ":" + level));
        document.append("customEnchants", customEnchants);
        document.append("material", this.getPickaxeMaterial().name());
        return document;
    }
}
