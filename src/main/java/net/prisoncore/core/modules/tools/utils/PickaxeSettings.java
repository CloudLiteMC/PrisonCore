package net.prisoncore.core.modules.tools.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.utils.exceptions.JsonDataNotFoundException;
import org.bukkit.Material;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;

import static net.prisoncore.core.utils.config.FileSystem.getJsonData;
import static net.prisoncore.core.utils.Utils.getDoubleFromConfigString;
import static net.prisoncore.core.utils.Utils.getMaterialFromConfigString;

public final class PickaxeSettings {

    private final PrisonCore core;
    private final HashMap<Material, Double> blocksExp;
    private final HashMap<Material, Double> tokenFinderBlocks;
    private int refreshRate;
    private double expIncreasePerLvl;
    private int maxLevel;

    private int maxEfficiencyLevel;
    private double efficiencyCostIncrease;
    private double efficiencyBaseCost;

    private int maxFortuneLevel;
    private double fortuneCostIncrease;
    private double fortuneBaseCost;

    private int maxNightVisionLevel;
    private double nightVisionCostIncrease;
    private double nightVisionBaseCost;

    private int maxSpeedLevel;
    private double speedCostIncrease;
    private double speedBaseCost;

    private int maxJumpLevel;
    private double jumpCostIncrease;
    private double jumpBaseCost;

    private int maxHasteLevel;
    private double hasteCostIncrease;
    private double hasteBaseCost;

    private int maxTokenFinderLevel;
    private double tokenFinderCostIncrease;
    private double tokenFinderBaseCost;

    public PickaxeSettings(@Nonnull final PrisonCore core) {
        this.core = core;
        this.core.getLog().log("&eLoading SuperPickaxe settings. . .");
        this.blocksExp = new HashMap<>();
        this.tokenFinderBlocks = new HashMap<>();
        final File file = new File(this.core.getDataFolder(), "/modules/superPickaxe.json");
        final JsonObject json = getJsonData(file);
        if (json == null) throw new JsonDataNotFoundException("Could not parse JSON data for JSON File /modules/superPickaxe.json");
        final JsonObject enchants = json.getAsJsonObject("enchants");

        this.loadBlockExp(json);
        this.loadTokenFinderBlocks(json);

        this.refreshRate = json.getAsJsonObject("settings").get("refreshRate").getAsInt();
        this.expIncreasePerLvl = json.getAsJsonObject("expSettings").get("expIncrease").getAsDouble();
        this.maxLevel = json.getAsJsonObject("expSettings").get("maxLevel").getAsInt();

        this.maxEfficiencyLevel = enchants.getAsJsonObject("efficiency").get("maxLevel").getAsInt();
        this.efficiencyCostIncrease = enchants.getAsJsonObject("efficiency").get("increasePerLevel").getAsDouble();
        this.efficiencyBaseCost = enchants.getAsJsonObject("efficiency").get("baseCost").getAsDouble();

        this.maxFortuneLevel = enchants.getAsJsonObject("fortune").get("maxLevel").getAsInt();
        this.fortuneCostIncrease = enchants.getAsJsonObject("fortune").get("increasePerLevel").getAsDouble();
        this.fortuneBaseCost = enchants.getAsJsonObject("fortune").get("baseCost").getAsDouble();

        this.maxNightVisionLevel = enchants.getAsJsonObject("nightVision").get("maxLevel").getAsInt();
        this.nightVisionCostIncrease = enchants.getAsJsonObject("nightVision").get("increasePerLevel").getAsDouble();
        this.nightVisionBaseCost = enchants.getAsJsonObject("nightVision").get("baseCost").getAsDouble();

        this.maxSpeedLevel = enchants.getAsJsonObject("speed").get("maxLevel").getAsInt();
        this.speedCostIncrease = enchants.getAsJsonObject("speed").get("increasePerLevel").getAsDouble();
        this.speedBaseCost = enchants.getAsJsonObject("speed").get("baseCost").getAsDouble();

        this.maxJumpLevel = enchants.getAsJsonObject("jump").get("maxLevel").getAsInt();
        this.jumpCostIncrease = enchants.getAsJsonObject("jump").get("increasePerLevel").getAsDouble();
        this.jumpBaseCost = enchants.getAsJsonObject("jump").get("baseCost").getAsDouble();

        this.maxHasteLevel = enchants.getAsJsonObject("haste").get("maxLevel").getAsInt();
        this.hasteCostIncrease = enchants.getAsJsonObject("haste").get("increasePerLevel").getAsDouble();
        this.hasteBaseCost = enchants.getAsJsonObject("haste").get("baseCost").getAsDouble();

        this.maxTokenFinderLevel = enchants.getAsJsonObject("tokenFinder").get("maxLevel").getAsInt();
        this.tokenFinderCostIncrease = enchants.getAsJsonObject("tokenFinder").get("increasePerLevel").getAsDouble();
        this.tokenFinderBaseCost = enchants.getAsJsonObject("tokenFinder").get("baseCost").getAsDouble();
    }

    /**
     * Loads all the blocks with SuperPickaxe EXP and puts them in a Map
     * @param json JsonObject that contains this data (superPickaxe.json)
     */
    private void loadBlockExp(@Nonnull final JsonObject json) {
        final JsonArray array = json.getAsJsonArray("blocksExp");
        array.forEach((element) -> {
            final Material mat = getMaterialFromConfigString(element.getAsString());
            final double expAmt = getDoubleFromConfigString(element.getAsString());
            blocksExp.putIfAbsent(mat, expAmt);
        });
        this.core.getLog().log("&bSuccessfully loaded &d" + this.blocksExp.size() + "&b Blocks with SuperPickaxe EXP");
    }

    /**
     * Loads all the blocks with SuperPickaxe TokeFinder data, and puts them in a map
     * @param json JsonObject that contains this data (superPickaxe.json)
     */
    public void loadTokenFinderBlocks(@Nonnull final JsonObject json) {
        final JsonArray array = json.getAsJsonObject("enchants").getAsJsonObject("tokenFinder").getAsJsonArray("blocks");
        array.forEach((element) -> {
            final Material mat = getMaterialFromConfigString(element.getAsString());
            final double tokenAmt = getDoubleFromConfigString(element.getAsString());
            this.tokenFinderBlocks.putIfAbsent(mat, tokenAmt);
        });
    }

    public final double getTokenFinderAmount(@Nonnull final Block block) {
        return this.getTokenFinderAmount(block.getType());
    }

    public final double getTokenFinderAmount(@Nonnull final Material mat) {
        if (this.tokenFinderBlocks.get(mat) == null) return 0D;
        return this.tokenFinderBlocks.get(mat);
    }

    public final double getExpForBlock(@Nonnull final Block block) {
        return this.getExpForBlock(block.getType());
    }

    public final double getExpForBlock(@Nonnull final Material mat) {
        if (this.blocksExp.get(mat) == null) return 0D;
        return this.blocksExp.get(mat);
    }

    public final int getRefreshRate() {
        return this.refreshRate;
    }

    public final double getExpIncreasePerLvl() {
        return this.expIncreasePerLvl;
    }

    public final int getMaxLevel() {
        return this.maxLevel;
    }

    /**
     * @return Gets the Max Efficiency level in settings
     */
    public final int getMaxEfficiencyLevel() {
        return this.maxEfficiencyLevel;
    }

    /**
     * @return Gets the efficiency cost increase in settings
     */
    public final double getEfficiencyCostIncrease() {
        return this.efficiencyCostIncrease;
    }

    /**
     * @return Gets the efficiency base cost in settings
     */
    public final double getEfficiencyBaseCost() {
        return this.efficiencyBaseCost;
    }

    /**
     * @return Gets the max fortune level in settings
     */
    public final int getMaxFortuneLevel() {
        return this.maxFortuneLevel;
    }

    /**
     * @return Gets the fortune cost increase in settings
     */
    public final double getFortuneCostIncrease() {
        return this.fortuneCostIncrease;
    }

    /**
     * @return Gets the fortune base cost in settings
     */
    public final double getFortuneBaseCost() {
        return this.fortuneBaseCost;
    }

    /**
     * @return Gets the max night vision level in settings
     */
    public final int getMaxNightVisionLevel() {
        return this.maxNightVisionLevel;
    }

    /**
     * @return Gets the night vision cost increase in settings
     */
    public final double getNightVisionCostIncrease() {
        return this.nightVisionCostIncrease;
    }

    /**
     * @return Gets the night vision base cost in config
     */
    public final double getNightVisionBaseCost() {
        return this.nightVisionBaseCost;
    }

    /**
     * @return Gets the max speed level in settings
     */
    public final int getMaxSpeedLevel() {
        return this.maxSpeedLevel;
    }

    /**
     * @return Gets the speed cost increase in settings
     */
    public final double getSpeedCostIncrease() {
        return this.speedCostIncrease;
    }

    /**
     * @return Gets the speed base cost in settings
     */
    public final double getSpeedBaseCost() {
        return this.speedBaseCost;
    }

    /**
     * @return Gets the max jump level in settings
     */
    public final int getMaxJumpLevel() {
        return this.maxJumpLevel;
    }

    /**
     * @return Gets the jump cost increase in settings
     */
    public final double getJumpCostIncrease() {
        return this.jumpCostIncrease;
    }

    /**
     * @return Gets the base jump cost in settings
     */
    public final double getJumpBaseCost() {
        return this.jumpBaseCost;
    }

    /**
     * @return Gets the max haste level in settings
     */
    public final int getMaxHasteLevel() {
        return this.maxHasteLevel;
    }

    /**
     * @return Gets the haste cost increase in settings
     */
    public final double getHasteCostIncrease() {
        return this.hasteCostIncrease;
    }

    /**
     * @return Gets the haste base cost in settings
     */
    public final double getHasteBaseCost() {
        return this.hasteBaseCost;
    }

    /**
     * @return Gets the token finder max level in settings
     */
    public final int getMaxTokenFinderLevel() {
        return this.maxTokenFinderLevel;
    }

    /**
     * @return Gets the token finder cost increase in settings
     */
    public final double getTokenFinderCostIncrease() {
        return this.tokenFinderCostIncrease;
    }

    /**
     * @return Gets the token finder base cost in settings
     */
    public final double getTokenFinderBaseCost() {
        return this.tokenFinderBaseCost;
    }
}
