package net.prisoncore.core.modules.shop.autosell.commands;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.shop.autosell.AutoSell;
import net.prisoncore.core.modules.shop.autosell.utils.AutoSellReport;
import net.prisoncore.core.utils.commandapi.PrisonCommand;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class AutoSellCommand extends PrisonCommand {

    private final PrisonCore core;

    public AutoSellCommand(@Nonnull final PrisonCore core) {
        super("autosell",
                "Main autosell command",
                "/autosell [<report>]");
        this.core = core;
        final List<String> aliases = new ArrayList<>();
        aliases.add("as");
        this.setAliases(aliases);
    }

    @Override
    public void onCommand(@Nonnull ConsoleCommandSender console, @Nonnull List<String> args) {
        Message.CONSOLE_NOT_ALLOWED.sendMessage(console, this.core);
    }

    @Override
    public void onCommand(@Nonnull Player player, @Nonnull List<String> args) {
        if (!Permission.AUTO_SELL_USE.has(player, this.core)) {
            Message.NO_PERMISSION.sendMessage(player, this.core);
            return;
        }

        final AutoSell autoSell = this.core.getAutoSell();
        final User user = this.core.getPlayerDataService().getUser(player.getUniqueId());
        if (args.size() == 0) {
            String toggled = "enabled";
            if (user.isAutoSellEnabled()) {
                toggled = "disabled";
                user.setAutoSell(false);
            } else user.setAutoSell(true);

            String message = Message.AUTO_SELL_TOGGLE.getMessage(this.core, true);
            message = message.replace("{0}", toggled);
            player.sendMessage(message);
        } else if (args.size() == 1) {
            if ("REPORT".equals(args.get(0).toUpperCase())) {
                final AutoSellReport report = autoSell.getReport(player);
                if (report == null) {
                    Message.NO_REPORT_EXISTS.sendMessage(player, this.core);
                    return;
                } else if (report.isEmpty()) {
                    Message.NO_REPORT_EXISTS.sendMessage(player, this.core);
                    return;
                }

                report.getReport().forEach(player::sendMessage);
            } else {
                String syntax = Message.INVALID_USAGE.getMessage(this.core, true);
                syntax = syntax.replace("{0}", "/autosell [<report>]");
                player.sendMessage(syntax);
            }
        } else {
            String syntax = Message.INVALID_USAGE.getMessage(this.core, true);
            syntax = syntax.replace("{0}", "/autosell [<report>]");
            player.sendMessage(syntax);
        }
    }
}
