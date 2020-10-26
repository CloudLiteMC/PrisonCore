package net.prisoncore.core.utils.economy;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import javax.annotation.Nonnull;

public class EconomyManager extends Module {

    private final PrisonCore core;
    private net.milkbowl.vault.economy.Economy economy;

    public EconomyManager(@Nonnull final PrisonCore core) {
        this.core = core;
    }

    @Override
    public Module init() {
        this.core.getLog().log("&3Initializing EconomyManagerModule. . .");
        this.economy = new Economy(this.core);
        Bukkit.getServicesManager().register(net.milkbowl.vault.economy.Economy.class, this.economy, this.core, ServicePriority.Normal);
        final RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) {
            throw new RuntimeException("Could not load VaultEconomy manager!");
        }
        this.economy = rsp.getProvider();
        this.core.getLog().log("&3EconomyManagerModule initialized!");
        return this;
    }

    @Override
    public void reload() {

    }

    public net.milkbowl.vault.economy.Economy getEconomy() {
        return this.economy;
    }
}
