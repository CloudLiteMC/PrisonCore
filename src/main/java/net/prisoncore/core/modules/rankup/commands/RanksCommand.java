package net.prisoncore.core.modules.rankup.commands;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.rankup.RankupModule;
import net.prisoncore.core.utils.commandapi.PrisonCommand;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import java.util.List;

import static net.prisoncore.core.utils.Utils.sendInvalidUsage;
import static net.prisoncore.core.utils.Utils.sendNoPermission;

public final class RanksCommand extends PrisonCommand {

    private RankupModule module;
    private final PrisonCore core;

    public RanksCommand(@Nonnull final RankupModule module) {
        super("ranks",
                "Lists all available ranks on the serer",
                "/ranks");
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
            sendInvalidUsage(player, "/ranks");
            return;
        }

        if (!Permission.LIST_RANKS.has(player, this.core)) {
            sendNoPermission(player);
            return;
        }

        player.openInventory(this.module.getRanksInventory(player));
    }
}
