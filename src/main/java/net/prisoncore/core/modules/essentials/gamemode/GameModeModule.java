package net.prisoncore.core.modules.essentials.gamemode;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.essentials.gamemode.commands.AdventureCommand;
import net.prisoncore.core.modules.essentials.gamemode.commands.CreativeCommand;
import net.prisoncore.core.modules.essentials.gamemode.commands.SpectatorCommand;
import net.prisoncore.core.modules.essentials.gamemode.commands.SurvivalCommand;
import net.prisoncore.core.utils.messages.Message;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

import static net.prisoncore.core.utils.Utils.playSound;

public final class GameModeModule extends Module {

    private final PrisonCore core;

    public GameModeModule(@Nonnull final PrisonCore core) {
        this.core = core;
    }

    @Override
    public Module init() {
        this.core.getLog().log("&3Initializing GameModeModule. . .");
        this.core.registerCommand("creative", new CreativeCommand(this));
        this.core.registerCommand("survival", new SurvivalCommand(this));
        this.core.registerCommand("adventure", new AdventureCommand(this));
        this.core.registerCommand("spectator", new SpectatorCommand(this));
        this.core.getLog().log("&3GameModeModule initialized!");
        return this;
    }

    @Override
    public void reload() {

    }

    public void updateGameMode(@Nonnull final Player target,
                               @Nonnull final CommandSender sender,
                               @Nonnull final GameMode gamemode) {

        if (target.getName().equalsIgnoreCase(sender.getName())) {
            String senderMessage = Message.SENDER_CONFIRM_TARGET_GAMEMODE_CHANGED.getMessage(this.core, true);
            senderMessage = senderMessage.replace("{0}", "Your");
            senderMessage = senderMessage.replace("{1}", gamemode.name());
            target.setGameMode(gamemode);
            playSound(target, Sound.ENTITY_VILLAGER_YES);
            target.sendMessage(senderMessage);
        } else {
            String targetMessage = Message.TARGET_GAMEMODE_CHANGED.getMessage(this.core, true);
            targetMessage = targetMessage.replace("{0}", gamemode.name());
            targetMessage = targetMessage.replace("{1}", "Console");
            target.setGameMode(gamemode);
            playSound(target, Sound.ENTITY_VILLAGER_YES);
            target.sendMessage(targetMessage);

            String senderMessage = Message.SENDER_CONFIRM_TARGET_GAMEMODE_CHANGED.getMessage(this.core, true);
            senderMessage = senderMessage.replace("{0}", target.getName() + "'s");
            senderMessage = senderMessage.replace("{1}", gamemode.name());
            sender.sendMessage(senderMessage);
        }
    }

    public final PrisonCore getCore() {
        return this.core;
    }
}
