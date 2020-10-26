package net.prisoncore.core.modules.essentials.economy.commands;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.utils.commandapi.PrisonCommand;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class PayCommand extends PrisonCommand {

    private final PrisonCore core;

    public PayCommand(@Nonnull final PrisonCore core) {
        super("pay",
                "Pays money to a player",
                "/pay <player> <amount>");
        this.core = core;
    }

    @Override
    public void onCommand(@Nonnull ConsoleCommandSender console, @Nonnull List<String> args) {
        Message.CONSOLE_NOT_ALLOWED.sendMessage(console, this.core);
    }

    @Override
    public void onCommand(@Nonnull Player player, @Nonnull List<String> args) {
        if (!Permission.PAY_PLAYER.has(player, this.core)) {
            Message.NO_PERMISSION.sendMessage(player, this.core);
            return;
        }

        if (args.size() != 2) {
            String usage = Message.INVALID_USAGE.getMessage(this.core, true);
            usage = usage.replace("{0}", "/pay <player> <amount>");
            player.sendMessage(usage);
            return;
        }

        final Player target = Bukkit.getPlayer(args.get(0));
        if (target == null) {
            Message.PLAYER_NOT_ONLINE.sendMessage(player, this.core);
            return;
        }

        int amt;
        try {
            amt = Integer.parseInt(args.get(1));
        } catch (@Nonnull final NumberFormatException ignored) {
            Message.INVALID_INTEGER.sendMessage(player, this.core);
            return;
        }

        if (amt <= 0) {
            Message.INVALID_INTEGER.sendMessage(player, this.core);
            return;
        }

        if (target == player) {

            return;
        }

        final User payer = this.core.getPlayerDataService().getUser(player.getUniqueId());
        final User receiver = this.core.getPlayerDataService().getUser(target.getUniqueId());
        payer.removeBalance(amt);
        receiver.addBalance(amt);

        String payerMessage = Message.PAYER_MESSAGE.getMessage(this.core, true);
        payerMessage = payerMessage.replace("{0}", String.valueOf(amt));
        payerMessage = payerMessage.replace("{1}", target.getName());
        player.sendMessage(payerMessage);

        String receiverMessage = Message.PAYER_RECEIVER_MESSAGE.getMessage(this.core, true);
        receiverMessage = receiverMessage.replace("{0}", String.valueOf(amt));
        receiverMessage = receiverMessage.replace("{1}", player.getName());
        target.sendMessage(receiverMessage);
    }
}
