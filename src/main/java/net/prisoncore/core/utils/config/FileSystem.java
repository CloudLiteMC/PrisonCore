package net.prisoncore.core.utils.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.prisoncore.core.PrisonCore;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public final class FileSystem {

    /**
     * Ensures that all of the plugins configuration files exist.
     * @param core PrisonCore instance to retrieve data folder
     */
    public static void checkConfigFiles(@Nonnull final PrisonCore core) {
        final File moduleDir = new File(core.getDataFolder(), "/modules");
        if (!moduleDir.exists() && moduleDir.mkdirs()) {
            core.getLog().log("&aCreated /modules directory!");
        }

        final File gameModeSettings = new File(core.getDataFolder(), "/modules/gameModeModule.json");
        if (!gameModeSettings.exists()) {
            core.saveResource("modules/gameModeModule.json", true);
            core.getLog().log("&aCreated new modules/gameModeModule.json file");
        }

        final File superPickaxeSettings = new File(core.getDataFolder(), "/modules/superPickaxe.json");
        if (!superPickaxeSettings.exists()) {
            core.saveResource("modules/superPickaxe.json", true);
            core.getLog().log("&aCreated new modules/superPickaxe.json file");
        }

        final File shopsSettings = new File(core.getDataFolder(), "/modules/shop.json");
        if (!shopsSettings.exists()) {
            core.saveResource("modules/shop.json", true);
            core.getLog().log("&aCreated new modules/shop.json file");
        }

        final File rankupSettings = new File(core.getDataFolder(), "/modules/rankup.json");
        if (!rankupSettings.exists()) {
            core.saveResource("modules/rankup.json", true);
            core.getLog().log("&aCreated new modules/rankup.json file");
        }

        final File mmoSettings = new File(core.getDataFolder(), "/modules/prisonMMO.json");
        if (!mmoSettings.exists()) {
            core.saveResource("modules/prisonMMO.json", true);
            core.getLog().log("&aCreated new modules/prisonMMO.json file");
        }

        final File fortuneSetting = new File(core.getDataFolder(), "/modules/fortune.json");
        if (!fortuneSetting.exists()) {
            core.saveResource("modules/fortune.json", true);
            core.getLog().log("&aCreated new modules/fortune.json file");
        }

        final File permissionSettings = new File(core.getDataFolder(), "permissions.json");
        if (!permissionSettings.exists()) {
            core.saveResource("permissions.json", true);
            core.getLog().log("&aCreated new permissions.json file");
        }

        final File messages = new File(core.getDataFolder(), "messages.json");
        if (!messages.exists()) {
            core.saveResource("messages.json", true);
            core.getLog().log("&aCreated new messages.json file");
        }

        final File serverSettings = new File(core.getDataFolder(), "serverSettings.json");
        if (!serverSettings.exists()) {
            core.saveResource("serverSettings.json", true);
            core.getLog().log("&aCreated new serverSettings.json");
        }
    }

    public static JsonObject getJsonData(@Nonnull final File file) {
        try {
            return new JsonParser().parse(new FileReader(file)).getAsJsonObject();
        } catch (@Nonnull final FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static JsonObject getJsonData(@Nonnull final Config config,
                                       @Nonnull final PrisonCore core) {
        switch (config) {
            case RANKUP: {
                final File file = new File(core.getDataFolder(), "/modules/rankup.json");
                return getJsonData(file);
            }

            case MMO: {
                final File file = new File(core.getDataFolder(), "/modules/prisonMMO.json");
                return getJsonData(file);
            }

            case FORTUNE: {
                final File file = new File(core.getDataFolder(), "/modules/fortune.json");
                return getJsonData(file);
            }

            default: { // server settings enum
                final File file = new File(core.getDataFolder(), "serverSettings.json");
                return getJsonData(file);
            }
        }
    }
}
