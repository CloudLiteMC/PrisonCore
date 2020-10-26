package net.prisoncore.core.modules.rankup;

import com.google.gson.JsonObject;
import net.luckperms.api.model.group.Group;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.rankup.commands.RanksCommand;
import net.prisoncore.core.modules.rankup.commands.RankupCommand;
import net.prisoncore.core.modules.rankup.events.ForcePlayerTrackEvent;
import net.prisoncore.core.modules.rankup.events.InventoryClicks;
import net.prisoncore.core.modules.rankup.utils.Rank;
import net.prisoncore.core.modules.rankup.utils.RankupInventory;
import net.prisoncore.core.utils.config.Config;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.messages.Message;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

import java.util.Collection;
import java.util.HashMap;

import static net.prisoncore.core.utils.Utils.*;
import static net.prisoncore.core.utils.config.FileSystem.getJsonData;

public final class RankupModule extends Module {

    private final PrisonCore core;
    private JsonObject rankupSettings;
    private ForcePlayerTrackEvent forcePlayerTrackEvent;
    private InventoryClicks inventoryClicks;
    private RankupInventory rankupInventory;

    private HashMap<String, Rank> rankups;
    private String defaultRankCommandSetter;

    public RankupModule(@Nonnull final PrisonCore core) {
        this.core = core;
    }


    @Override
    public Module init() {
        this.core.getLog().log("&3Initializing RankUp Module. . .");
        this.rankupSettings = getJsonData(Config.RANKUP, this.core);
        this.defaultRankCommandSetter = this.rankupSettings.get("defaultRankCommandHandle").getAsString();
        this.loadRanks();
        this.rankupInventory = new RankupInventory(this);

        this.core.registerCommand("ranks", new RanksCommand(this));
        this.core.registerCommand("rankup", new RankupCommand(this));

        this.forcePlayerTrackEvent = new ForcePlayerTrackEvent(this);
        this.core.registerEvent(this.forcePlayerTrackEvent);
        this.inventoryClicks = new InventoryClicks();
        this.core.registerEvent(this.inventoryClicks);

        this.core.getLog().log("&3RankUp Module Initialized!");
        return this;
    }

    @Override
    public void reload() {
        this.rankupSettings = getJsonData(Config.RANKUP, this.core);
        this.defaultRankCommandSetter = this.rankupSettings.get("defaultRankCommandHandle").getAsString();
        this.loadRanks();
        this.rankupInventory = new RankupInventory(this);

        HandlerList.unregisterAll(this.forcePlayerTrackEvent);
        this.forcePlayerTrackEvent = new ForcePlayerTrackEvent(this);
        this.core.registerEvent(this.forcePlayerTrackEvent);

        HandlerList.unregisterAll(this.inventoryClicks);
        this.inventoryClicks = new InventoryClicks();
        this.core.registerEvent(this.inventoryClicks);
    }

    private void loadRanks() {
        this.rankups = new HashMap<>();
        this.rankupSettings.get("ranks").getAsJsonArray().forEach((element) -> {
            final Rank rank = new Rank(element.getAsJsonObject());
            this.rankups.putIfAbsent(rank.getThisRankName().toUpperCase(), rank);
        });
        this.core.getLog().log("&3Successfully loaded &e" + this.rankups.size() + "&3 RankUps!");
    }

    public final String getDefaultRankCommand(@Nonnull final Player player) {
        return this.defaultRankCommandSetter.replace("{player}", player.getName());
    }

    public final Collection<Rank> getAllRanks() {
        return this.rankups.values();
    }

    /**
     * Attempts to rankup a player
     * @param p Player to rankup
     */
    public void attemptRankup(@Nonnull final Player p) {
        final User user = this.core.getPlayerDataService().getUser(p.getUniqueId());
        final Group userGroup = getLuckPermsGroup(p.getUniqueId());
        final Rank userRankup = this.getRankup(userGroup.getName().toUpperCase());
        if (userRankup == null) {
            Message.RANKUP_MAX_RANK.sendMessage(p, this.core);
            this.core.getLog().log("&e" + p.getName() + "&3 Did not rankup. &7(&6Already max rank&7)");
            return;
        }

        final double userBalance = user.getBalance();
        final double rankupPrice = userRankup.getRankupCost();

        if (userBalance < rankupPrice) {
            Message.RANK_NOT_ENOUGH_MONEY.sendMessage(p, this.core);
            this.core.getLog().log("&e" + p.getName() + "&3 Did not rankup. &7(&6Not Enough Money&7)");
            return;
        }

        user.removeBalance(rankupPrice);
        userRankup.getCommands().forEach((command) -> {
            command = command.replace("{player}", p.getName());
            sendConsoleCommand(command);
        });
        if (userRankup.isDoBroadcast()) {
            Bukkit.broadcastMessage(userRankup.getBroadcastMessage(p));
        }

        p.sendMessage(userRankup.getPlayerRankupMessage());
        playSound(p, Sound.ITEM_CHORUS_FRUIT_TELEPORT);
    }

    public final Inventory getRanksInventory(@Nonnull final Player p) {
        return this.rankupInventory.getInventory(p);
    }

    /**
     * Gets a Rank from the cache
     * @param groupName player's group name
     * @return null if player is at max rank
     */
    public Rank getRankup(@Nonnull final String groupName) {
        return this.rankups.get(groupName);
    }

    public final PrisonCore getCore() {
        return this.core;
    }
}
