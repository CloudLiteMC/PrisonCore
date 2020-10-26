package net.prisoncore.core.modules.essentials.feed.commands;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.essentials.feed.FeedModule;
import net.prisoncore.core.utils.commandapi.PrisonCommand;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

import static net.prisoncore.core.utils.Utils.sendInvalidUsage;
import static net.prisoncore.core.utils.Utils.sendNoPermission;

public class FeedCommand extends PrisonCommand {

    private final FeedModule module;
    private final PrisonCore core;

    public FeedCommand(@Nonnull final FeedModule module) {
        super("feed",
                "Feeds you or another player",
                "/feed [<player>]");
        this.module = module;
        this.core = this.module.getCore();
    }


    @Override
    public void onCommand(@Nonnull ConsoleCommandSender console, @Nonnull List<String> args) {
        if (args.size() == 0) {
            Message.CONSOLE_NOT_ALLOWED.sendMessage(console, this.core);
            return;
        }

        if (args.size() == 1) {
            final Player target = Bukkit.getPlayer(args.get(0));
            if (target == null) {
                Message.PLAYER_NOT_ONLINE.sendMessage(console, this.core);
                return;
            }

            this.module.feedPlayer(target, console);
            return;
        }

        sendInvalidUsage(console, "/feed <player>");
    }

    @Override
    public void onCommand(@Nonnull Player player, @Nonnull List<String> args) {
        if (args.size() == 0) {
            if (!Permission.FEED_SELF.has(player, this.core)) {
                sendNoPermission(player);
                return;
            }

            this.module.feedPlayer(player, null);
            return;
        }

        if (args.size() == 1) {
            if (!Permission.FEED_OTHER.has(player, this.core)) {
                sendNoPermission(player);
                return;
            }

            final Player target = Bukkit.getPlayer(args.get(0));
            if (target == null) {
                Message.PLAYER_NOT_ONLINE.sendMessage(player, this.core);
                return;
            }

            this.module.feedPlayer(target, player);
            return;
        }

        sendInvalidUsage(player, "/feed [<player>]");
    }
}
