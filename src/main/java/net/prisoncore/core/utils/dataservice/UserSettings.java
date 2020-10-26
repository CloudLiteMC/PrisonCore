package net.prisoncore.core.utils.dataservice;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public final class UserSettings {

    private final User user;
    private final PrisonCore core;

    private boolean autoSellEnabled;

    public UserSettings(@Nonnull final User user) {
        // TODO: Later down the line make default settings loaded
        this.user = user;
        this.core = this.user.getCore();
        this.update();
    }

    /**
     * Loads the UserSettings variables
     */
    public void update() {
        final Player p = this.user.getPlayer();
        if (p == null) {
            this.autoSellEnabled = false;
            return;
        }

        this.autoSellEnabled = Permission.AUTO_SELL_USE.has(p, this.core);

        if (this.autoSellEnabled) {
            Message.AUTO_SELL_ENABLE_DEFAULT.sendMessage(p, this.core);
        }
    }

    /**
     * @return true is this player has autosell enabled
     */
    public final boolean isAutoSellEnabled() {
        return this.autoSellEnabled;
    }

    /**
     * @param enabled true to enable AutoSell
     */
    public void setAutoSellEnabled(final boolean enabled) {
        this.autoSellEnabled = enabled;
    }
}
