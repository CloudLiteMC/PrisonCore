package net.prisoncore.core.modules.essentials.heal;

import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.essentials.heal.commands.HealCommand;
import net.prisoncore.core.utils.BukkitTimer;
import net.prisoncore.core.utils.config.Config;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

import static net.prisoncore.core.utils.Utils.sendInvalidUsage;
import static net.prisoncore.core.utils.config.FileSystem.getJsonData;

public class HealModule extends Module {

    private final PrisonCore core;
    private final HashMap<UUID, HealTimer> playersInCooldown;
    private JsonObject serverSettings;

    private int healCooldown;

    public HealModule(@Nonnull final PrisonCore core) {
        this.core = core;
        this.playersInCooldown = new HashMap<>();
    }

    @Override
    public Module init() {
        this.core.registerCommand("heal", new HealCommand(this));
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
        this.serverSettings = getJsonData(Config.SERVER_SETTINGS, this.core).get("heal").getAsJsonObject();

        this.healCooldown = this.serverSettings.get("cooldown").getAsInt();
    }

    public void healPlayer(@Nonnull final Player effected,
                           @Nullable final CommandSender sender) {

        if (sender instanceof Player) {
            final Player senderPlayer = ((Player) sender);
            if (senderPlayer.getUniqueId().equals(effected.getUniqueId())) {
                sendInvalidUsage(senderPlayer, "/heal");
                return;
            }

            Message.TARGET_HEALED_MESSAGE.sendMessage(effected, this.core);
            this.sendSenderMessage(sender, effected.getName());
            effected.setHealth(20D);
        } else if (sender instanceof ConsoleCommandSender) {
            Message.TARGET_HEALED_MESSAGE.sendMessage(effected, this.core);
            this.sendSenderMessage(sender, effected.getName());
            effected.setHealth(20D);
        } else {
            if (Permission.BYPASS_HEAL_COOLDOWN.has(effected, this.core)) {
                Message.TARGET_HEALED_MESSAGE.sendMessage(effected, this.core);
                effected.setHealth(20D);
                return;
            }

            if (this.isOnCooldown(effected)) {
                this.sendCooldownMessage(effected);
                return;
            }

            Message.TARGET_HEALED_MESSAGE.sendMessage(effected, this.core);
            this.addPlayerToCooldown(effected);
            effected.setHealth(20D);
        }
    }

    public void sendSenderMessage(@Nonnull final CommandSender sender,
                                  @Nonnull final String playerName) {
        String msg = Message.SENDER_HEALED_MESSAGE.getMessage(this.core, true);
        msg = msg.replace("{0}", playerName);
        sender.sendMessage(msg);
    }

    public void sendCooldownMessage(@Nonnull final Player player) {
        final int timeLeft = this.getTimeRemaining(player);
        String message = Message.PLAYER_ON_HEAL_TIMER.getMessage(this.core, true);
        message = message.replace("{0}", String.valueOf(timeLeft));
        player.sendMessage(message);
    }


    public void addPlayerToCooldown(@Nonnull final Player playerToAdd) {
        if (this.playersInCooldown.get(playerToAdd.getUniqueId()) != null) return;
        this.playersInCooldown.putIfAbsent(playerToAdd.getUniqueId(), new HealTimer(this, playerToAdd.getUniqueId()));
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
        final HealTimer timer = this.playersInCooldown.get(player.getUniqueId());
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

    public final int getHealCooldown() {
        return this.healCooldown;
    }
}
