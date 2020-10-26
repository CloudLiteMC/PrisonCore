package net.prisoncore.core.modules.essentials.clearinventory.commands;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.ModuleType;
import net.prisoncore.core.modules.essentials.clearinventory.ClearInventoryModule;
import net.prisoncore.core.modules.essentials.clearinventory.utils.ClearInventoryHolder;
import net.prisoncore.core.utils.commandapi.PrisonCommand;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static net.prisoncore.core.utils.Utils.textOfRaw;

public final class ClearInventoryCommand extends PrisonCommand {

    private final PrisonCore core;
    private ClearInventoryModule clearInventoryModule;

    public ClearInventoryCommand(@Nonnull final PrisonCore core,
                                 @Nonnull final ClearInventoryModule module) {
        super("clearinventory",
                "Clear's yours or another player's inventory",
                "/clearinventory [<player>]");
        this.core = core;
        this.clearInventoryModule = module;
        final List<String> aliases = new ArrayList<>();
        aliases.add("ci");
        aliases.add("clearinv");
        this.setAliases(aliases);
    }


    @Override
    public void onCommand(@Nonnull ConsoleCommandSender console, @Nonnull List<String> args) {
        if (args.size() == 0) {
            Message.CONSOLE_NOT_ALLOWED.sendMessage(console, this.core);
        } else if (args.size() == 1) {
            final Player p = Bukkit.getPlayer(args.get(0));
            if (p == null) {
                Message.PLAYER_NOT_ONLINE.sendMessage(console, this.core);
            } else {
                this.clearInventoryModule.clearInventory(this.core.getPlayerDataService().getUser(p.getUniqueId()));

                String senderMessage = Message.SENDER_INVENTORY_CLEAR_COMPLETE.getMessage(this.core, true);
                senderMessage = senderMessage.replace("{0}", p.getName());
                console.sendMessage(senderMessage);
            }
        } else {
            String message = Message.INVALID_USAGE.getMessage(this.core, true);
            message = message.replace("{0}", "/clearinventory <player>");
            console.sendMessage(message);
        }
    }

    @Override
    public void onCommand(@Nonnull Player player, @Nonnull List<String> args) {
        if (args.size() == 0) {
            if (!Permission.CLEAR_INVENTORY_SELF.has(player, this.core)) {
                Message.NO_PERMISSION.sendMessage(player, this.core);
                return;
            }

            player.openInventory(this.getInventoryPrompt());
        } else if (args.size() == 1) {
            if (!Permission.CLEAR_INVENTORY_OTHER.has(player, this.core)) {
                Message.NO_PERMISSION.sendMessage(player, this.core);
                return;
            }

            final Player target = Bukkit.getPlayer(args.get(0));
            if (target == null) {
                Message.PLAYER_NOT_ONLINE.sendMessage(player, this.core);
                return;
            }

            if (target.getName().equalsIgnoreCase(player.getName())) {
                this.clearInventoryModule.clearInventory(this.core.getPlayerDataService().getUser(player.getUniqueId()));
                return;
            }

            this.clearInventoryModule.clearInventory(this.core.getPlayerDataService().getUser(target.getUniqueId()));
            String senderMessage = Message.SENDER_INVENTORY_CLEAR_COMPLETE.getMessage(this.core, true);
            senderMessage = senderMessage.replace("{0}", target.getName());
            player.sendMessage(senderMessage);
        } else {
            String message = Message.INVALID_USAGE.getMessage(this.core, true);
            message = message.replace("{0}", "/clearinventory <player>");
            player.sendMessage(message);
        }
    }

    private Inventory getInventoryPrompt() {
        final Inventory inv = Bukkit.createInventory(new ClearInventoryHolder(), 27, textOfRaw("&e&lConfirm to clear inventory?"));
        final ItemStack border = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        final ItemMeta borderMeta = border.getItemMeta();
        borderMeta.setDisplayName(textOfRaw("&f "));
        border.setItemMeta(borderMeta);
        final ItemStack yesItem = new ItemStack(Material.EMERALD_BLOCK, 1);
        final ItemMeta yesMeta = yesItem.getItemMeta();
        yesMeta.setDisplayName(textOfRaw("&a&lYES"));
        yesItem.setItemMeta(yesMeta);
        final ItemStack noItem = new ItemStack(Material.REDSTONE_BLOCK, 1);
        final ItemMeta noMeta = noItem.getItemMeta();
        noMeta.setDisplayName(textOfRaw("&c&lNO"));
        noItem.setItemMeta(noMeta);

        for (int x = 0; x <= 8; x++) {
            inv.setItem(x, border);
        }
        inv.setItem(9, border);
        inv.setItem(10, border);
        inv.setItem(11, yesItem);
        inv.setItem(12, border);
        inv.setItem(13, border);
        inv.setItem(14, border);
        inv.setItem(15, noItem);
        inv.setItem(16, border);
        inv.setItem(17, border);
        for (int x = 18; x <= 26; x++) {
            inv.setItem(x, border);
        }
        return inv;
    }
}
