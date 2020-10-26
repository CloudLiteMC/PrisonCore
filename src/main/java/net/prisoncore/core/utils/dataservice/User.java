package net.prisoncore.core.utils.dataservice;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.tools.pickaxe.SuperPickaxe;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public class User {
    private final Document document;
    private final SuperPickaxe playerPickaxe;
    private final UserMMO mmo;
    private final UserSettings settings;
    private final PrisonCore core;

    public User(final Document document, @Nonnull final PrisonCore core) {
        this.core = core;
        this.document = document;
        this.playerPickaxe = new SuperPickaxe(((Document) this.document.get("pickaxe")), this.core);
        this.mmo = new UserMMO((Document) this.document.get("prisonMmo"));
        this.settings = new UserSettings(this);
    }

    //TODO: To increase data resilience any calls to get need to be null checked, if null set the default value
    //TODO: this is to prevent any runtime NPE's, but for development this will be "okayish"

    public final UUID getUuid() {
        return UUID.fromString(this.document.getString("uuid"));
    }

    public final Player getPlayer() {
        return Bukkit.getPlayer(this.getUuid());
    }

    public final double getBalance() { return this.document.getDouble("balance"); }

    public void removeBalance(final double amount) {
        this.document.replace("balance", this.getBalance() - amount);
    }

    public void addBalance(final double amount) {
        this.document.replace("balance", this.getBalance() + amount);
    }

    public void setBalance(final double amount) {
        this.document.replace("balance", amount);
    }

    public double getTokens() { return this.document.getDouble("tokens"); }

    public void addTokens(final double amount) {
        this.document.replace("tokens", this.getTokens() + amount);
    }

    public void removeTokens(final double amount) {
        this.document.replace("tokens", this.getTokens() - amount);
    }

    public final double getAutoSellMultiplier() { return this.document.getDouble("autoSellMultiplier"); }

    public void addAutoSellMultiplier(final double amount) { this.document.replace("autoSellMultiplier", this.getAutoSellMultiplier() + amount); }

    public void removeAutoSellMultiplier(final double amount) { this.document.replace("autoSellMultiplier", this.getAutoSellMultiplier() - amount); }

    public Document getDocument() {
        this.document.replace("pickaxe", this.getPickaxe().createDatabaseDocument());
        this.document.replace("prisonMmo", this.getMMO().getDatabaseDocument());
        return this.document;
    }

    public SuperPickaxe getPickaxe() { return this.playerPickaxe; }

    public UserMMO getMMO() { return this.mmo; }

    /**
     * @return true if AutoSell is enabled
     */
    public boolean isAutoSellEnabled() {
        return this.settings.isAutoSellEnabled();
    }

    /**
     * Sets wether or not AutoSell should be on or off
     * @param enable true if AutoSell should be on
     */
    public void setAutoSell(final boolean enable) {
        this.settings.setAutoSellEnabled(enable);
    }

    public final PrisonCore getCore() {
        return this.core;
    }
}
