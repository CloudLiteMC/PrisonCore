package net.prisoncore.core.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.utils.exceptions.MaterialNotFoundException;
import net.prisoncore.core.utils.messages.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

public final class Utils {

    private final static LuckPerms luckPerms = LuckPermsProvider.get();

    /**
     * converts a normal string to colored without prefix
     * @param message string to colorize
     * @return colorized string
     */
    public static @Nonnull String textOfRaw(@Nonnull final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * converts a normal string to colored without prefix
     * @param message string to colorize
     * @return colorized string
     */
    public static @Nonnull String textOf(@Nonnull final String message) {
        return ChatColor.translateAlternateColorCodes('&', "&3&lCLOUD-LITE &b&l>> &r" + message);
    }

    /**
     * @param line config string from config file (MATERIAL:DATA)
     * @return Material from a config line, line variable must be in "MATERIAL:<other-data>" format
     */
    public static Material getMaterialFromConfigString(@Nonnull final String line) {
        final String[] obj = line.split(":");
        final Material mat = Material.getMaterial(obj[0]);
        if (mat == null) throw new MaterialNotFoundException("Material could not be found: " + obj[0]);
        return mat;
    }

    /**
     * @param line config string from config file (DATA:DOUBLE)
     * @return Double from config line, line variable must be in "<other-data>:DOUBLE" format
     */
    public static Double getDoubleFromConfigString(@Nonnull final String line) {
        final String[] obj = line.split(":");
        return Double.parseDouble(obj[1]);
    }

    /**
     * Checks if an item is a type of pickaxe os not
     * @param toCheck ItemStack to check
     * @return true if toCheck is a piakcaxe
     */
    public static boolean isPickaxe(final ItemStack toCheck) {
        if (toCheck == null) return false;
        return toCheck.getType().equals(Material.WOODEN_PICKAXE) ||
                toCheck.getType().equals(Material.STONE_PICKAXE) ||
                toCheck.getType().equals(Material.IRON_PICKAXE) ||
                toCheck.getType().equals(Material.GOLDEN_PICKAXE) ||
                toCheck.getType().equals(Material.DIAMOND_PICKAXE) ||
                toCheck.getType().equals(Material.NETHERITE_PICKAXE);
    }

    /**
     * Plays a sound to player
     * @param target Player to send sound to
     * @param sound Sound to send
     */
    public static void playSound(@Nonnull final Player target, @Nonnull final Sound sound) {
        target.playSound(target.getLocation(), sound, 1.0F, 1.0F);
    }

    /**
     * Sends the invalid command syntax message to the player
     * this method was solely created to reduce the code length
     * @param s CommandSender
     * @param u Usage string
     */
    public static void sendInvalidUsage(@Nonnull final CommandSender s, @Nonnull final String u) {
        String syntax = Message.INVALID_USAGE.getMessage(PrisonCore.getStaticInstance(), true);
        syntax = syntax.replace("{0}", u);
        s.sendMessage(syntax);
    }

    public static Group getLuckPermsGroup(@Nonnull final UUID playerUuid) {
        return luckPerms.getGroupManager().getGroup(Objects.requireNonNull(luckPerms.getUserManager().getUser(playerUuid)).getPrimaryGroup());
    }

    public static int getLuckPermsGroupWeight(@Nonnull final String group) {
        final Group g = luckPerms.getGroupManager().getGroup(group);
        return g.getWeight().getAsInt();
    }

    /**
     * Sends the no permission message along with the VILLAGER_NO sound
     * @param p Player
     */
    public static void sendNoPermission(@Nonnull final Player p) {
        playSound(p, Sound.ENTITY_VILLAGER_NO);
        Message.NO_PERMISSION.sendMessage(p, PrisonCore.getStaticInstance());
    }

    public static void sendMessage(@Nonnull final Player target, @Nonnull final String message) {
        target.sendMessage(message);
    }

    public static void sendMessage(@Nonnull final ConsoleCommandSender target, @Nonnull final String message) {
        target.sendMessage(message);
    }

    /**
     * Sends a command as console
     * @param command to send
     */
    public static void sendConsoleCommand(@Nonnull final String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public static int getPercentOf(final int percent, final int number) {
        return (int)(number*(percent/100.0f));
    }

    public static String getPrettyNumber(final int number) {
        return getPrettyNumber((double) number);
    }

    public static String getPrettyNumber(final double number) {
        char[] suffix = {' ', 'K', 'M', 'B', 'T', 'P', 'E'};
        long numValue = (long)number;
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }
}

