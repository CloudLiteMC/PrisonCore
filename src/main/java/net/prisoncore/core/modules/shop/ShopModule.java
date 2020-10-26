package net.prisoncore.core.modules.shop;

import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.shop.autosell.AutoSell;
import net.prisoncore.core.modules.shop.autosell.commands.AutoSellCommand;
import net.prisoncore.core.utils.exceptions.JsonDataNotFoundException;
import net.prisoncore.core.utils.exceptions.MaterialNotFoundException;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;

import static net.prisoncore.core.utils.config.FileSystem.getJsonData;

public final class ShopModule extends Module {

    private final PrisonCore core;
    private HashMap<Material, Double> shopPrices;

    private AutoSell autoSell;

    public ShopModule(@Nonnull final PrisonCore core) {
        this.core = core;
        this.shopPrices = new HashMap<>();

        final File file = new File(this.core.getDataFolder(), "/modules/shop.json");
        final JsonObject data = getJsonData(file);
        if (data == null) throw new JsonDataNotFoundException("Could not parse JSON data for /modules/shop.json");
        data.getAsJsonArray("prices").forEach((line) -> {
            final String[] split = line.getAsString().split(":");
            final Material material = Material.getMaterial(split[0]);
            if (material == null) throw new MaterialNotFoundException("Invalid material: " + split[0]);
            final double price = Double.parseDouble(split[1]);
            this.shopPrices.putIfAbsent(material, price);
        });

        this.core.getLog().log("&3Successfully loaded &e" + this.shopPrices.size() + " &3shop items and they're prices!");
    }

    @Override
    public Module init() {
        this.core.getLog().log("&3Initializing ShopModule. . .");
        this.autoSell = new AutoSell(this);
        this.core.registerCommand("autosell", new AutoSellCommand(this.core));
        this.core.getLog().log("&3ShopModule initialized!");
        return this;
    }

    @Override
    public void reload() {
        HandlerList.unregisterAll(this.autoSell);
        this.autoSell =  new AutoSell(this);
    }

    /**
     * Gets the shop sell price for a material
     * @param material to check
     * @return -1D if a material is not able to be sold.
     */
    public final double getPriceForItem(@Nonnull final Material material) {
        if (this.shopPrices.get(material) == null) return -1D;
        return this.shopPrices.get(material);
    }

    /**
     * @return AutoSell instance
     */
    public final AutoSell getAutoSell() {
        return this.autoSell;
    }

    /**
     * @return Plugin instance
     */
    public final PrisonCore getCore() {
        return this.core;
    }
}
