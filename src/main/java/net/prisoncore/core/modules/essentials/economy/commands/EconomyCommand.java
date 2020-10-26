package net.prisoncore.core.modules.essentials.economy.commands;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.utils.commandapi.PrisonCommand;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class EconomyCommand extends PrisonCommand {

    private final PrisonCore core;

    public EconomyCommand(@Nonnull final PrisonCore core) {
        super("economy",
                "Main economy command",
                "/economy <give|take|set> <player> <amount>");
        this.core = core;
        final List<String> aliases = new ArrayList<>();
        aliases.add("eco");
        this.setAliases(aliases);
    }

    @Override
    public void onCommand(@Nonnull ConsoleCommandSender console, @Nonnull List<String> args) {
        if (args.size() != 3) {
            String usage = Message.INVALID_USAGE.getMessage(this.core, true);
            usage = usage.replace("{0}", "/economy <give|take|set> <player> <amount>");
            console.sendMessage(usage);
            return;
        }

        final Player target = Bukkit.getPlayer(args.get(1));
        if (target == null) {
            Message.PLAYER_NOT_ONLINE.sendMessage(console, this.core);
            return;
        }

        int money;
        try {
            money = Integer.parseInt(args.get(2));
        } catch (@Nonnull final NumberFormatException ignored) {
            Message.INVALID_INTEGER.sendMessage(console, this.core);
            return;
        }

        final User targetUser = this.core.getPlayerDataService().getUser(target.getUniqueId());
        this.handleAction(args.get(0), targetUser, console, money);

    }

    @Override
    public void onCommand(@Nonnull Player player, @Nonnull List<String> args) {
        if (!Permission.ECONOMY_ADMIN.has(player, this.core)) {
            Message.NO_PERMISSION.sendMessage(player, this.core);
            return;
        }

        if (args.size() != 3) {
            String syntax = Message.INVALID_USAGE.getMessage(this.core, true);
            syntax = syntax.replace("{0}", "/economy <give|take|set> <player> <amount>");
            player.sendMessage(syntax);
            return;
        }

        final Player p = Bukkit.getPlayer(args.get(1));
        if (p == null) {
            Message.PLAYER_NOT_ONLINE.sendMessage(player, this.core);
            return;
        }

        int money;
        try {
            money = Integer.parseInt(args.get(2));
        } catch (@Nonnull final NumberFormatException ignored) {
            Message.INVALID_INTEGER.sendMessage(player, this.core);
            return;
        }

        final User targetUser = this.core.getPlayerDataService().getUser(p.getUniqueId());
        this.handleAction(args.get(0), targetUser, player, money);
    }

    private void handleAction(@Nonnull final String action,
                              @Nonnull final User target,
                              @Nonnull final CommandSender sender,
                              final int money) {
        final Player targetPlayer = target.getPlayer();
        switch (action.toUpperCase()) {

            case "GIVE": {
                target.addBalance(money);
                String receiverMessage = Message.ADMIN_MONEY_RECEIVER_GIVE.getMessage(this.core, true);
                receiverMessage = receiverMessage.replace("{0}", String.valueOf(money));
                targetPlayer.sendMessage(receiverMessage);
                String senderMessage = Message.ADMIN_MONEY_SENDER_GIVE.getMessage(this.core, true);
                senderMessage = senderMessage.replace("{0}", String.valueOf(money));
                senderMessage = senderMessage.replace("{1}", targetPlayer.getName());
                sender.sendMessage(senderMessage);
                break;
            }

            case "TAKE": {
                target.removeBalance(money);
                String receiverMessage = Message.ADMIN_MONEY_RECEIVER_TAKE.getMessage(this.core, true);
                receiverMessage = receiverMessage.replace("{0}", String.valueOf(money));
                targetPlayer.sendMessage(receiverMessage);
                String senderMessage = Message.ADMIN_MONEY_SENDER_TAKE.getMessage(this.core, true);
                senderMessage = senderMessage.replace("{0}", String.valueOf(money));
                senderMessage = senderMessage.replace("{1}", targetPlayer.getName());
                sender.sendMessage(senderMessage);
                break;
            }

            case "SET": {
                target.setBalance(money);
                String receiverMessage = Message.ADMIN_MONEY_RECEIVER_SET.getMessage(this.core, true);
                receiverMessage = receiverMessage.replace("{0}", String.valueOf(money));
                targetPlayer.sendMessage(receiverMessage);
                String senderMessage = Message.ADMIN_MONEY_SENDER_SET.getMessage(this.core, true);
                senderMessage = senderMessage.replace("{0}", targetPlayer.getName());
                senderMessage = senderMessage.replace("{1}", String.valueOf(money));
                sender.sendMessage(senderMessage);
                break;
            }

            default: {
                String syntax = Message.INVALID_USAGE.getMessage(this.core, true);
                syntax = syntax.replace("{0}", "/economy <give|take|set> <player> <amount>");
                sender.sendMessage(syntax);
                break;
            }
        }
    }
}
