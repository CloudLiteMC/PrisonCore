package net.prisoncore.core.utils.permission;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.ModuleType;
import net.prisoncore.core.utils.exceptions.PermissionNotFoundException;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public enum Permission {

    CREATIVE_SELF("modules.gamemode.creativeSelf"),
    CREATIVE_OTHER("modules.gamemode.creativeOther"),
    SURVIVAL_SELF("modules.gamemode.survivalSelf"),
    SURVIVAL_OTHER("modules.gamemode.survivalOther"),
    ADVENTURE_SELF("modules.gamemode.adventureSelf"),
    ADVENTURE_OTHER("modules.gamemode.adventureOther"),
    SPECTATOR_SELF("modules.gamemode.spectatorSelf"),
    SPECTATOR_OTHER("modules.gamemode.spectatorOther"),

    CHECK_BALANCE_SELF("modules.economy.checkBalanceSelf"),
    CHECK_BALANCE_OTHER("modules.economy.checkBalanceOther"),
    PAY_PLAYER("modules.economy.payPlayer"),
    ECONOMY_ADMIN("modules.economy.economyAdmin"),

    COLOR_CHAT("modules.chat.colorChat"),

    DISPLAY_MMO_GUI("modules.prisonMMO.displayMMOGui"),

    AUTO_SELL_USE("modules.shop.autoSell.useAutoSell"),

    HEAL_SELF("modules.heal.self"),
    HEAL_OTHER("modules.heal.other"),
    BYPASS_HEAL_COOLDOWN("modules.heal.bypassCooldown"),

    FEED_SELF("modules.feed.self"),
    FEED_OTHER("modules.feed.other"),
    BYPASS_FEED_COOLDOWN("modules.feed.bypassCooldown"),

    RANKUP("modules.rankup.use"),
    LIST_RANKS("modules.rankup.listRanks"),

    DISPOSE_OWN_PICKAXE("modules.superpickaxe.dispose"),

    CLEAR_INVENTORY_SELF("modules.clearInventory.self"),
    CLEAR_INVENTORY_OTHER("modules.clearInventory.other");

    private String path;

    Permission(@Nonnull final String path) {
        this.path = path;

    }

    public String getPermission(@Nonnull final PrisonCore core) {
        final PermissionManager manager = core.getPermissionManager();
        JsonObject obj = manager.getPermissionsObject();
        JsonElement ele = null;
        for (final String field : this.path.split("\\.")) {
            if (ele == null) {
                ele = obj.get(field);
            } else {
                JsonObject temp = ele.getAsJsonObject();
                ele = temp.get(field);
            }
        }
        if (ele == null) throw new PermissionNotFoundException("Permission not found: " + this.path);
        return ele.getAsString();
    }

    public boolean has(@Nonnull final Player p, @Nonnull final PrisonCore core) {
        final String permission = this.getPermission(core);
        final PermissionManager manager = ((PermissionManager) core.getModule(ModuleType.PERMISSIONS));
        return manager.hasPermission(p, permission);
    }
}
