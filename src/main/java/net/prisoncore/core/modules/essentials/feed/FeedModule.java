package net.prisoncore.core.modules.essentials.feed;

import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.essentials.feed.commands.FeedCommand;
import net.prisoncore.core.utils.config.Config;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

import static net.prisoncore.core.utils.Utils.sendInvalidUsage;
import static net.prisoncore.core.utils.config.FileSystem.getJsonData;

public class FeedModule extends Module {

    private final PrisonCore core;
    private final HashMap<UUID, FeedTimer> playersInCooldown;
    private JsonObject serverSettings;

    private int feedCooldown;

    public FeedModule(@Nonnull final PrisonCore core) {
        this.core = core;
        this.playersInCooldown = new HashMap<>();
    }

    @Override
    public Module init() {
        this.core.registerCommand("feed", new FeedCommand(this));
        this.loadSettings();
        return this;
    }

    @Override
    public void reload() {
        this.loadSettings();
    }

    /**
     * Loads all the server settings
     */
    private void loadSettings() {
        this.serverSettings = getJsonData(Config.SERVER_SETTINGS, this.core).get("feed").getAsJsonObject();

        this.feedCooldown = this.serverSettings.get("cooldown").getAsInt();
    }

    public void feedPlayer(@Nonnull final Player effected,
                           @Nullable final CommandSender sender) {

        if (sender instanceof Player) {
            final Player senderPlayer = ((Player) sender);
            if (senderPlayer.getUniqueId().equals(effected.getUniqueId())) {
                sendInvalidUsage(senderPlayer, "/feed");
                return;
            }

            Message.TARGET_FEED_MESSAGE.sendMessage(effected, this.core);
            this.sendSenderMessage(sender, effected.getName());
            effected.setFoodLevel(20);
        } else if (sender instanceof ConsoleCommandSender) {
            Message.TARGET_FEED_MESSAGE.sendMessage(effected, this.core);
            this.sendSenderMessage(sender, effected.getName());
            effected.setFoodLevel(20);
        } else {
            if (Permission.BYPASS_FEED_COOLDOWN.has(effected, this.core)) {
                Message.TARGET_FEED_MESSAGE.sendMessage(effected, this.core);
                effected.setFoodLevel(20);
                return;
            }

            if (this.isOnCooldown(effected)) {
                this.sendCooldownMessage(effected);
                return;
            }

            Message.TARGET_FEED_MESSAGE.sendMessage(effected, this.core);
            this.addPlayerToCooldown(effected);
            effected.setFoodLevel(20);
        }
    }

    public void sendSenderMessage(@Nonnull final CommandSender sender,
                                  @Nonnull final String playerName) {
        String msg = Message.SENDER_FEED_MESSAGE.getMessage(this.core, true);
        msg = msg.replace("{0}", playerName);
        sender.sendMessage(msg);
    }

    public void sendCooldownMessage(@Nonnull final Player player) {
        final int timeLeft = this.getTimeRemaining(player);
        String message = Message.PLAYER_ON_FEED_TIMER.getMessage(this.core, true);
        message = message.replace("{0}", String.valueOf(timeLeft));
        player.sendMessage(message);
    }


    public void addPlayerToCooldown(@Nonnull final Player playerToAdd) {
        if (this.playersInCooldown.get(playerToAdd.getUniqueId()) != null) return;
        this.playersInCooldown.putIfAbsent(playerToAdd.getUniqueId(), new FeedTimer(this, playerToAdd.getUniqueId()));
    }

    /**
     * Checks if a player is on cooldown
     * @param player to check
     * @return true if player is on cooldown
     */
    public final boolean isOnCooldown(@Nonnull final Player player) {
        return this.playersInCooldown.get(player.getUniqueId()) != null;
    }

    /**
     * Gets the amount of time a player has left in cooldown
     * @param player to use
     * @return 0 if player is not in cooldown
     */
    public final int getTimeRemaining(@Nonnull final Player player) {
        if (this.playersInCooldown.get(player.getUniqueId()) == null) return 0;
        final FeedTimer timer = this.playersInCooldown.get(player.getUniqueId());
        return timer.getTimeLeft();
    }

    public void clearTimer(@Nonnull final UUID uuid) {
        if (this.playersInCooldown.get(uuid) == null) return;
        this.playersInCooldown.get(uuid).clear();
        this.playersInCooldown.remove(uuid);
    }

    /**
     * @return The plugin instance
     */
    public final PrisonCore getCore() {
        return this.core;
    }

    public final int getFeedCooldown() {
        return this.feedCooldown;
    }
}
