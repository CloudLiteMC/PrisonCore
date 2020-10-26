package net.prisoncore.core.modules.essentials.heal.commands;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.essentials.heal.HealModule;
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

public class HealCommand extends PrisonCommand {

    private final HealModule module;
    private final PrisonCore core;

    public HealCommand(@Nonnull final HealModule module) {
        super("heal",
                "Heals you or another player",
                "/heal [<player>]");
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

            this.module.healPlayer(target, console);
            return;
        }

        sendInvalidUsage(console, "/heal <player>");
    }

    @Override
    public void onCommand(@Nonnull Player player, @Nonnull List<String> args) {
        if (args.size() == 0) {
            if (!Permission.HEAL_SELF.has(player, this.core)) {
                sendNoPermission(player);
                return;
            }

            this.module.healPlayer(player, null);
            return;
        }

        if (args.size() == 1) {
            if (!Permission.HEAL_OTHER.has(player, this.core)) {
                sendNoPermission(player);
                return;
            }

            final Player target = Bukkit.getPlayer(args.get(0));
            if (target == null) {
                Message.PLAYER_NOT_ONLINE.sendMessage(player, this.core);
                return;
            }

            this.module.healPlayer(target, player);
            return;
        }

        sendInvalidUsage(player, "/heal [<player>]");
    }
}
