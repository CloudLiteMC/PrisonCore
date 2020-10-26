package net.prisoncore.core.utils.permission;

import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import javax.annotation.Nonnull;
import java.io.File;

import static net.prisoncore.core.utils.config.FileSystem.getJsonData;

public final class PermissionManager extends Module {

    private final PrisonCore core;
    private static PrisonCore staticCore;
    private JsonObject permissions;
    private static net.milkbowl.vault.permission.Permission perms;

    public PermissionManager(@Nonnull final PrisonCore core) {
        this.core = core;
        staticCore = core;
    }

    @Override
    public Module init() {
        this.loadVaultPermission();
        this.load();
        return this;
    }

    @Override
    public void reload() {
        this.load();
    }

    private void load() {
        final File file = new File(this.core.getDataFolder(), "permissions.json");
        this.permissions = getJsonData(file);
    }

    /**
     * Loads the Vault Permissions service
     */
    private void loadVaultPermission() {
        final RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> rsp = this.core.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (rsp == null) {
            throw new RuntimeException("Could not load VaultPermission manager!");
        }
        perms = rsp.getProvider();
    }

    /**
     * @return permissions.json contents
     */
    public final JsonObject getPermissionsObject() {
        return this.permissions;
    }

    /**
     * checks a players permission
     * @param player player to check
     * @param permission permission to check
     * @return true if player has permission
     */
    public boolean hasPermission(@Nonnull final Player player, @Nonnull final String permission) {
        return perms.playerHas(player, permission);
    }

    /**
     * checks a command senders permission
     * @param sender sender to check
     * @param permission permission to check
     * @return true if player has permission
     */
    public boolean hasPermission(@Nonnull final CommandSender sender, @Nonnull final String permission) {
        return perms.has(sender, permission);
    }

}
