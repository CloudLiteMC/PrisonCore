package net.prisoncore.core;

import net.milkbowl.vault.economy.Economy;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.ModuleType;
import net.prisoncore.core.modules.essentials.chat.ChatModule;
import net.prisoncore.core.modules.essentials.clearinventory.ClearInventoryModule;
import net.prisoncore.core.modules.essentials.economy.EconomyModule;
import net.prisoncore.core.modules.essentials.feed.FeedModule;
import net.prisoncore.core.modules.essentials.gamemode.GameModeModule;
import net.prisoncore.core.modules.essentials.heal.HealModule;
import net.prisoncore.core.modules.essentials.motd.ServerMotdModule;
import net.prisoncore.core.modules.prisonmmo.PrisonMMOModule;
import net.prisoncore.core.modules.rankup.RankupModule;
import net.prisoncore.core.modules.shop.ShopModule;
import net.prisoncore.core.modules.shop.autosell.AutoSell;
import net.prisoncore.core.modules.tools.ToolsModule;
import net.prisoncore.core.utils.Logger;
import net.prisoncore.core.utils.commandapi.PrisonCommand;
import net.prisoncore.core.utils.dataservice.PlayerDataService;
import net.prisoncore.core.utils.economy.EconomyManager;
import net.prisoncore.core.utils.exceptions.ModuleNotFoundException;
import net.prisoncore.core.utils.fortune.FortuneModule;
import net.prisoncore.core.utils.messages.MessageManager;
import net.prisoncore.core.utils.permission.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;

import static net.prisoncore.core.utils.config.FileSystem.checkConfigFiles;

public final class PrisonCore extends JavaPlugin {

    private static PrisonCore staticInstance;
    private final Logger logger = new Logger("&3PrisonCore");
    private HashMap<ModuleType, Module> modules;

    @Override
    public void onEnable() {
        this.logger.log("&eInitializing PrisonCore modules. . .");
        staticInstance = this;
        checkConfigFiles(this);
        this.modules = new HashMap<>();

        this.modules.putIfAbsent(ModuleType.PLAYER_DATA_SERVICE, new PlayerDataService(this).init());
        this.modules.putIfAbsent(ModuleType.ECONOMY, new EconomyManager(this).init());
        this.modules.putIfAbsent(ModuleType.ECONOMY_COMMANDS, new EconomyModule(this).init());
        this.modules.putIfAbsent(ModuleType.PERMISSIONS, new PermissionManager(this).init());
        this.modules.putIfAbsent(ModuleType.MESSAGES, new MessageManager(this).init());
        this.modules.putIfAbsent(ModuleType.TOOLS, new ToolsModule(this).init());
        this.modules.putIfAbsent(ModuleType.GAMEMODE, new GameModeModule(this).init());
        this.modules.putIfAbsent(ModuleType.CLEAR_INVENTORY, new ClearInventoryModule(this).init());
        this.modules.putIfAbsent(ModuleType.MOTD, new ServerMotdModule(this).init());
        this.modules.putIfAbsent(ModuleType.SHOP, new ShopModule(this).init());
        this.modules.putIfAbsent(ModuleType.HEAL, new HealModule(this).init());
        this.modules.putIfAbsent(ModuleType.FEED, new FeedModule(this).init());
        this.modules.putIfAbsent(ModuleType.RANKUP, new RankupModule(this).init());
        this.modules.putIfAbsent(ModuleType.CHAT, new ChatModule(this).init());
        this.modules.putIfAbsent(ModuleType.MMO, new PrisonMMOModule(this).init());
        this.modules.putIfAbsent(ModuleType.FORTUNE_BLOCKS, new FortuneModule(this).init());
        this.logger.log("&aPrisonCore modules have been initialized!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Gets a module during runtime to use
     * @param moduleType ModuleType to use
     * @return instance of any class extending Module
     * @throws ModuleNotFoundException gets thrown if a module cannot be found, this is likely due to it not being enabled
     */
    public Object getModule(@Nonnull final ModuleType moduleType) throws ModuleNotFoundException {
        final Object obj = this.modules.get(moduleType);
        if (obj == null) throw new ModuleNotFoundException(moduleType);
        return obj;
    }

    /**
     * Enables a command from the plugin.yml
     * @param commandName name of the command
     * @param command PrisonCommand command to load
     */
    public void registerCommand(@Nonnull final String commandName, @Nonnull final PrisonCommand command) {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            final CommandMap commandMap = ((CommandMap) commandMapField.get(Bukkit.getServer()));
            commandMap.register(commandName, command);
        } catch (@Nonnull final IllegalAccessException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Register's an event to the server
     * @param listener Bukkit Listener to register
     */
    public void registerEvent(@Nonnull final Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * @return Logger instance of this class
     */
    public @Nonnull final Logger getLog() {
        return this.logger;
    }

    /**
     * @return PermissionManager instance of this plugin
     */
    public final PermissionManager getPermissionManager() {
        try {
            return ((PermissionManager) this.getModule(ModuleType.PERMISSIONS));
        } catch (@Nonnull final ModuleNotFoundException ex) {
            ex.printStackTrace();
            return this.getPermissionManager();
        }
    }

    /**
     * @return PlayerDataService instance of this plugin
     */
    public final PlayerDataService getPlayerDataService() {
        try {
            return ((PlayerDataService) this.getModule(ModuleType.PLAYER_DATA_SERVICE));
        } catch (@Nonnull final ModuleNotFoundException ex) {
            ex.printStackTrace();
            return this.getPlayerDataService();
        }
    }

    /**
     * @return Vault Economy instance of this plugin
     */
    public final Economy getEconomy() {
        try {
            final EconomyManager manager = ((EconomyManager) this.getModule(ModuleType.ECONOMY));
            return manager.getEconomy();
        } catch (@Nonnull final ModuleNotFoundException ex) {
            ex.printStackTrace();
            return this.getEconomy();
        }
    }

    public final ShopModule getShopModule() {
        try {
            final ShopModule module = ((ShopModule) this.getModule(ModuleType.SHOP));
            return module;
        } catch (@Nonnull final ModuleNotFoundException ex) {
            ex.printStackTrace();
            return this.getShopModule();
        }
    }

    public final AutoSell getAutoSell() {
        return this.getShopModule().getAutoSell();
    }

    public static PrisonCore getStaticInstance() {
        return staticInstance;
    }
}
