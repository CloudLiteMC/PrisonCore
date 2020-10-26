package net.prisoncore.core.modules.prisonmmo;

import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.prisonmmo.commands.MMOCommand;
import net.prisoncore.core.modules.prisonmmo.modules.mining.MiningMMOModule;
import net.prisoncore.core.modules.prisonmmo.modules.player.PlayerMMOModule;
import net.prisoncore.core.modules.prisonmmo.utils.ExpQueueWorker;
import net.prisoncore.core.modules.prisonmmo.utils.MMOGui;
import net.prisoncore.core.modules.prisonmmo.utils.QueueType;
import net.prisoncore.core.utils.config.Config;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.dataservice.UserMMO;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

import java.util.UUID;

import static net.prisoncore.core.utils.config.FileSystem.getJsonData;

public final class PrisonMMOModule extends Module {

    private final PrisonCore core;

    private PlayerMMOModule playerModule;
    private MiningMMOModule miningModule;
    private MMOGui mmoGui;
    private ExpQueueWorker worker;

    private double expPerPlayerLevel;
    private double expPerMiningLevel;

    public PrisonMMOModule(@Nonnull final PrisonCore core) {
        this.core = core;
    }

    @Override
    public Module init() {
        this.core.getLog().log("&3Initializing PrisonMMO Module. . .");
        this.mmoGui = new MMOGui(this);
        this.worker = new ExpQueueWorker(this);

        final JsonObject obj = getJsonData(Config.MMO, this.core);

        this.expPerPlayerLevel = obj.getAsJsonObject("player").get("expIncrease").getAsDouble();
        this.expPerMiningLevel = obj.getAsJsonObject("mining").get("expIncrease").getAsDouble();

        this.core.registerCommand("mmo", new MMOCommand(this));
        this.playerModule = new PlayerMMOModule(this);
        this.miningModule = new MiningMMOModule(this);

        this.core.getLog().log("&3PrisonMMO Module Initialized!");
        return this;
    }

    @Override
    public void reload() {
        this.mmoGui = new MMOGui(this);
        this.worker = new ExpQueueWorker(this);

        final JsonObject obj = getJsonData(Config.MMO, this.core);

        this.expPerPlayerLevel = obj.getAsJsonObject("player").get("expIncrease").getAsDouble();
        this.expPerMiningLevel = obj.getAsJsonObject("mining").get("expIncrease").getAsDouble();

        this.playerModule = new PlayerMMOModule(this);
        this.miningModule = new MiningMMOModule(this);
    }

    public final double getPlayerExpIncrease() {
        return this.expPerPlayerLevel;
    }

    public double getPlayerExpNeeded(@Nonnull final UserMMO mmo) {
        return ((mmo.getPlayerLevel() * this.expPerPlayerLevel) - mmo.getPlayerExp());
    }

    public void addPlayerExp(@Nonnull final UUID uuid, final double amount) {
        this.worker.addExpToQueue(uuid, QueueType.PLAYER, amount);
    }

    public final double getMiningExpIncrease() {
        return this.expPerMiningLevel;
    }

    public double getMiningExpNeeded(@Nonnull final UserMMO mmo) {
        return ((mmo.getMiningLevel() * this.expPerMiningLevel) - mmo.getMiningExp());
    }

    public void addMiningExp(@Nonnull final UUID uuid, final double amount) {
        this.worker.addExpToQueue(uuid, QueueType.MINING, amount);
    }

    public final Inventory getMainPage(@Nonnull final User user) {
        return this.mmoGui.getMainPage(user);
    }

    public final PrisonCore getCore() {
        return this.core;
    }
}
