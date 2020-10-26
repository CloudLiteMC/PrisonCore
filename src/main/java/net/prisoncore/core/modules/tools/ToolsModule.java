package net.prisoncore.core.modules.tools;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.tools.pickaxe.events.PickaxeUpgradeEventHandler;
import net.prisoncore.core.modules.tools.pickaxe.events.PreventPickaxeRemoval;
import net.prisoncore.core.modules.tools.pickaxe.events.SuperPickaxeEventHandler;
import net.prisoncore.core.modules.tools.utils.PickaxeSettings;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public final class ToolsModule extends Module {

    private final PrisonCore core;
    private PickaxeSettings pickaxeSettings;
    private PreventPickaxeRemoval preventPickaxeRemoval;
    private SuperPickaxeEventHandler superPickaxeEventHandler;
    private PickaxeUpgradeEventHandler pickaxeUpgradeEventHandler;

    public ToolsModule(@Nonnull final PrisonCore core) {
        this.core = core;
    }

    @Override
    public Module init() {
        this.core.getLog().log("&3Initializing ToolsModule. . .");
        this.pickaxeSettings = new PickaxeSettings(this.core);

        this.preventPickaxeRemoval = new PreventPickaxeRemoval(this.core);
        this.core.registerEvent(this.preventPickaxeRemoval);

        this.superPickaxeEventHandler = new SuperPickaxeEventHandler(this.core, this.pickaxeSettings);
        this.core.registerEvent(this.superPickaxeEventHandler);

        this.pickaxeUpgradeEventHandler = new PickaxeUpgradeEventHandler(this.core, this.pickaxeSettings);
        this.core.registerEvent(this.pickaxeUpgradeEventHandler);
        this.core.getLog().log("&3ToolsModule initialized!");
        return this;
    }

    @Override
    public void reload() {
        this.pickaxeSettings = new PickaxeSettings(this.core);

        HandlerList.unregisterAll(this.preventPickaxeRemoval);
        this.preventPickaxeRemoval = new PreventPickaxeRemoval(this.core);
        this.core.registerEvent(this.preventPickaxeRemoval);

        HandlerList.unregisterAll(this.superPickaxeEventHandler);
        this.superPickaxeEventHandler = new SuperPickaxeEventHandler(this.core, this.pickaxeSettings);
        this.core.registerEvent(this.superPickaxeEventHandler);

        HandlerList.unregisterAll(this.pickaxeUpgradeEventHandler);
        this.pickaxeUpgradeEventHandler = new PickaxeUpgradeEventHandler(this.core, this.pickaxeSettings);
        this.core.registerEvent(this.pickaxeUpgradeEventHandler);
    }

    /**
     * @return PickaxeSettings instance of this plugin
     */
    public @Nonnull final PickaxeSettings getPickaxeSettings() {
        return this.pickaxeSettings;
    }
}
