package net.prisoncore.core.modules.shop.autosell;

import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.shop.ShopModule;
import net.prisoncore.core.modules.shop.autosell.events.PlayerBreaksBlock;
import net.prisoncore.core.modules.shop.autosell.utils.AutoSellReport;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import static net.prisoncore.core.utils.config.FileSystem.getJsonData;

public final class AutoSell implements Listener {

    private final ShopModule shopModule;
    private final PrisonCore core;
    private final HashMap<UUID, AutoSellReport> playerAutoSellReports;

    private int refreshRate;

    public AutoSell(@Nonnull final ShopModule module) {
        this.shopModule = module;
        this.core = this.shopModule.getCore();

        final File file = new File(this.core.getDataFolder(), "/modules/shop.json");
        final JsonObject data = getJsonData(file);

        this.refreshRate = data.get("autoSellRefreshRate").getAsInt();

        this.core.registerEvent(this);
        this.core.registerEvent(new PlayerBreaksBlock(this));
        this.playerAutoSellReports = new HashMap<>();
    }

    /**
     * Updates an existing report, or inserts a new one for Player
     * @param playerUuid effected UUID
     * @param report AutoSellReport to insert
     */
    public void updateReport(@Nonnull final UUID playerUuid, @Nonnull final AutoSellReport report) {
        if (this.playerAutoSellReports.get(playerUuid) == null){
            this.playerAutoSellReports.put(playerUuid, report);
            return;
        }
        this.playerAutoSellReports.replace(playerUuid, report);
    }

    /**
     * @param player Player to get Report of
     * @return null if no report exists
     */
    public AutoSellReport getReport(@Nonnull final Player player) {
        return this.playerAutoSellReports.get(player.getUniqueId());
    }

    /**
     * @return Rate at in which to send AutoSellReport's (in seconds)
     */
    public int getRefreshRate() {
        return this.refreshRate;
    }

    /**
     * @return Plugin instance
     */
    public final PrisonCore getCore() {
        return this.core;
    }

    /**
     * @return ShopModule instance
     */
    public final ShopModule getShopModule() {
        return this.shopModule;
    }
}
