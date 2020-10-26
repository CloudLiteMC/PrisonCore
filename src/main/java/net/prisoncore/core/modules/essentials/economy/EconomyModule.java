package net.prisoncore.core.modules.essentials.economy;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.essentials.economy.commands.BalanceCommand;
import net.prisoncore.core.modules.essentials.economy.commands.EconomyCommand;
import net.prisoncore.core.modules.essentials.economy.commands.PayCommand;

import javax.annotation.Nonnull;

public final class EconomyModule extends Module {

    private final PrisonCore core;

    public EconomyModule(@Nonnull final PrisonCore core) {
        this.core = core;
    }

    @Override
    public Module init() {
        this.core.getLog().log("&3Initializing EconomyModuleCommands. . .");
        this.core.registerCommand("balance", new BalanceCommand(this.core));
        this.core.registerCommand("pay", new PayCommand(this.core));
        this.core.registerCommand("economy", new EconomyCommand(this.core));
        this.core.getLog().log("&3EconomyModuleCommands initialized!");
        return this;
    }

    @Override
    public void reload() {

    }
}
