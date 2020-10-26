package net.prisoncore.core.utils;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;

import static net.prisoncore.core.utils.Utils.textOfRaw;

public final class Logger {

    private final String prefix;

    public Logger(@Nonnull final String logPrefix) {
        this.prefix = logPrefix;
    }

    /**
     * @param msg message to log to console
     */
    public void log(@Nonnull final String msg) {
        Bukkit.getLogger().info(textOfRaw(this.prefix + "&r " + msg));
    }
}
