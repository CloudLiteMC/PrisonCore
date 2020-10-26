package net.prisoncore.core.modules.shop.autosell.utils;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.prisoncore.core.utils.Utils.getPrettyNumber;
import static net.prisoncore.core.utils.Utils.textOfRaw;

public final class AutoSellReport {

    private final HashMap<Material, Integer> amountSold;
    private final HashMap<Material, Double> priceSoldFor;

    public AutoSellReport() {
        this.amountSold = new HashMap<>();
        this.priceSoldFor = new HashMap<>();
    }

    /**
     * @return true is this report is empty currently
     */
    public boolean isEmpty() {
        return this.amountSold.isEmpty();
    }

    /**
     * Adds data to this report
     * @param material to add
     * @param amountSold of material
     * @param priceSold for
     */
    public void addToReport(@Nonnull final Material material, final int amountSold, final double priceSold) {
        if (this.amountSold.get(material) == null) this.amountSold.putIfAbsent(material, amountSold);
        if (this.priceSoldFor.get(material) == null) this.priceSoldFor.putIfAbsent(material, priceSold);

        this.amountSold.put(material, this.amountSold.get(material) + amountSold);
        this.priceSoldFor.put(material, this.priceSoldFor.get(material) + priceSold);
    }

    /**
     * Converts all the data from HashMaps to a viewable
     * ArrayList
     * @return List of Strings
     */
    public List<String> getReport() {
        final List<String> report = new ArrayList<>();
        double total = 0D;
        report.add(textOfRaw("&3&lAUTO SELL REPORT"));
        report.add(textOfRaw("&f "));
        this.amountSold.forEach((material, amtSold) -> {
            final double price = this.priceSoldFor.get(material);
            report.add(textOfRaw("&a&lSOLD &cx&e" + getPrettyNumber(amtSold) + "&7&l " + material.name() + "&7 for &2&l$ &a&l" + getPrettyNumber(price)));
        });
        for (final double price : this.priceSoldFor.values()) {
            total += price;
        }
        report.add(textOfRaw("&f "));
        report.add(textOfRaw("&e&lTotal money made &2&l$ &a&l" + getPrettyNumber(total)));
        return report;
    }
}
