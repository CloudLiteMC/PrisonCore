package net.prisoncore.core.modules.rankup.commands;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.rankup.RankupModule;
import net.prisoncore.core.utils.commandapi.PrisonCommand;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

import static net.prisoncore.core.utils.Utils.sendInvalidUsage;
import static net.prisoncore.core.utils.Utils.sendNoPermission;

public final class RankupCommand extends PrisonCommand {

    private final RankupModule module;
    private final PrisonCore core;

    public RankupCommand(@Nonnull final RankupModule module) {
        super("rankup",
                "Rankup message",
                "/rankup");
        this.module = module;
        this.core = this.module.getCore();
    }

    @Override
    public void onCommand(@Nonnull ConsoleCommandSender console, @Nonnull List<String> args) {
        Message.CONSOLE_NOT_ALLOWED.sendMessage(console, this.core);
    }

    @Override
    public void onCommand(@Nonnull Player player, @Nonnull List<String> args) {
        if (args.size() != 0) {
            sendInvalidUsage(player, "/rankup");
            return;
        }

        if (!Permission.RANKUP.has(player, this.core)) {
            sendNoPermission(player);
            return;
        }

        this.core.getLog().log("&e" + player.getName() + "&3 Is attempting to RankUp!");
        this.module.attemptRankup(player);
    }
}
