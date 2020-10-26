package net.prisoncore.core.modules.essentials.gamemode.commands;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.ModuleType;
import net.prisoncore.core.modules.essentials.gamemode.GameModeModule;
import net.prisoncore.core.utils.commandapi.PrisonCommand;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static net.prisoncore.core.utils.Utils.*;
import static net.prisoncore.core.utils.Utils.sendNoPermission;

public final class CreativeCommand extends PrisonCommand {

    private final GameModeModule gameModeModule;
    private final PrisonCore core;

    public CreativeCommand(@Nonnull final GameModeModule module) {
        super("creative",
                "Change's you or another player's gamemode to creative!",
                "/creative [<player>]");
        this.gameModeModule = module;
        this.core = this.gameModeModule.getCore();
        final List<String> aliases = new ArrayList<>();
        aliases.add("gmc");
        aliases.add("gmcreative");
        this.setAliases(aliases);
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
            } else this.gameModeModule.updateGameMode(target, console, GameMode.CREATIVE);
            return;
        }

        sendInvalidUsage(console, "/gmc <player>");
    }

    @Override
    public void onCommand(@Nonnull Player player, @Nonnull List<String> args) {
        final boolean creativeSelf = Permission.CREATIVE_SELF.has(player, this.core);
        final boolean creativeOther = Permission.CREATIVE_OTHER.has(player, this.core);

        if (args.size() == 0) {
            if (!creativeSelf) {
                sendNoPermission(player);
                return;
            }
            this.gameModeModule.updateGameMode(player, player, GameMode.CREATIVE);
            return;
        }

        if (args.size() == 1) {
            if (!creativeOther) {
                sendNoPermission(player);
                return;
            }

            final Player target = Bukkit.getPlayer(args.get(0));
            if (target == null) {
                Message.PLAYER_NOT_ONLINE.sendMessage(player, this.core);
            } else {
                this.gameModeModule.updateGameMode(target, player, GameMode.CREATIVE);
            }
            return;
        }

        sendInvalidUsage(player, "/gmc [<player>]");
    }
}
