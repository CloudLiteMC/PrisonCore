package net.prisoncore.core.utils.commandapi;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.utils.dataservice.PlayerDataService;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.prisoncore.core.utils.Utils.textOf;

public abstract class PrisonCommand extends BukkitCommand {

    protected PrisonCommand(@Nonnull final String command,
                            @Nonnull final String desc,
                            @Nonnull final String usage) {
        super(command);
        this.description = desc;
        this.usageMessage = textOf("&cIncorrect usage! &e" + usage);
    }

    @Override
    public boolean execute(@Nonnull final CommandSender sender, @Nonnull final String commandLabel, @Nonnull final String[] args) {
        final List<String> newArgs = new ArrayList<>(Arrays.asList(args));
        if (sender instanceof Player) {
            final Player p = ((Player) sender);
            this.onCommand(p, newArgs);
        } else {
            final ConsoleCommandSender c = ((ConsoleCommandSender) sender);
            this.onCommand(c, newArgs);
        }
        return true;
    }

    public abstract void onCommand(@Nonnull final ConsoleCommandSender console, @Nonnull final List<String> args);

    public abstract void onCommand(@Nonnull final Player player, @Nonnull final List<String> args);
}
