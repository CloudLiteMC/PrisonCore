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
import java.util.ArrayList;
import java.util.List;

public final class BalanceCommand extends PrisonCommand {

    private final PrisonCore core;

    public BalanceCommand(@Nonnull final PrisonCore core) {
        super("balance",
                "Checks you or another player's balance",
                "/balance [<player>]");
        this.core = core;
        final List<String> aliases = new ArrayList<>();
        aliases.add("bal");
        aliases.add("money");
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
                return;
            }

            final User u = this.core.getPlayerDataService().getUser(p.getUniqueId());
            String balanceMsg = Message.BALANCE_OTHER_PLAYER.getMessage(this.core, true);
            balanceMsg = balanceMsg.replace("{0}", p.getName());
            balanceMsg = balanceMsg.replace("{1}", String.valueOf((int) u.getBalance()));
            console.sendMessage(balanceMsg);
        } else {
            String syntax = Message.INVALID_USAGE.getMessage(this.core, true);
            syntax = syntax.replace("{0}", "/balance <player>");
            console.sendMessage(syntax);
        }
    }

    @Override
    public void onCommand(@Nonnull Player player, @Nonnull List<String> args) {
        if (args.size() == 0) {
            if (!Permission.CHECK_BALANCE_SELF.has(player, this.core)) {
                Message.NO_PERMISSION.sendMessage(player, this.core);
                return;
            }

            final User u = this.core.getPlayerDataService().getUser(player.getUniqueId());
            final int balance = (int) u.getBalance();
            String balanceString = Message.BALANCE_SELF_PLAYER.getMessage(this.core, true);
            balanceString = balanceString.replace("{0}", String.valueOf(balance));
            player.sendMessage(balanceString);
        } else if (args.size() == 1) {
            if (!Permission.CHECK_BALANCE_OTHER.has(player, this.core)) {
                Message.NO_PERMISSION.sendMessage(player, this.core);
                return;
            }

            final Player target = Bukkit.getPlayer(args.get(0));
            if (target == null) {
                Message.PLAYER_NOT_ONLINE.sendMessage(player, this.core);
                return;
            }

            final User user = this.core.getPlayerDataService().getUser(target.getUniqueId());
            final int targetBalance = (int) user.getBalance();
            String balanceString = Message.BALANCE_OTHER_PLAYER.getMessage(this.core, true);
            balanceString = balanceString.replace("{0}", target.getName());
            balanceString = balanceString.replace("{1}", String.valueOf(targetBalance));
            player.sendMessage(balanceString);
        } else {
            String syntax = Message.INVALID_USAGE.getMessage(this.core, true);
            syntax = syntax.replace("{0}", "/balance [<player>]");
            player.sendMessage(syntax);
        }
    }
}
