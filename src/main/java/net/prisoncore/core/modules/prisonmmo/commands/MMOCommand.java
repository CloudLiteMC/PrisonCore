package net.prisoncore.core.modules.prisonmmo.commands;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.prisonmmo.PrisonMMOModule;
import net.prisoncore.core.utils.commandapi.PrisonCommand;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class MMOCommand extends PrisonCommand {

    private final PrisonMMOModule module;
    private PrisonCore core;

    public MMOCommand(@Nonnull final PrisonMMOModule module) {
        super("mmo",
                "Main PrisonMMO Command",
                "/mmo");
        this.module = module;
        this.core = module.getCore();
        final List<String> aliases = new ArrayList<>();
        aliases.add("mcmmo");
        aliases.add("mcrpg");
        aliases.add("prisonmmo");
        aliases.add("prisonrpg");
        aliases.add("pmmo");
        aliases.add("prpg");
        this.setAliases(aliases);
    }


    @Override
    public void onCommand(@Nonnull ConsoleCommandSender console, @Nonnull List<String> args) {

    }

    @Override
    public void onCommand(@Nonnull Player player, @Nonnull List<String> args) {
        if (args.size() == 0) {

            if (!Permission.DISPLAY_MMO_GUI.has(player, this.core)) {
                Message.NO_PERMISSION.sendMessage(player, this.core);
                return;
            }

            final User user = this.core.getPlayerDataService().getUser(player.getUniqueId());
            player.openInventory(this.module.getMainPage(user));
        }
    }
}
