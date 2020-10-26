package net.prisoncore.core.modules.essentials.chat.events;

import net.luckperms.api.model.group.Group;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.essentials.chat.ChatModule;
import net.prisoncore.core.utils.messages.Message;
import net.prisoncore.core.utils.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.annotation.Nonnull;

import static net.prisoncore.core.utils.Utils.getLuckPermsGroup;
import static net.prisoncore.core.utils.Utils.textOfRaw;

public class ChatEvent implements Listener {

    private final ChatModule module;
    private final PrisonCore core;

    public ChatEvent(@Nonnull final ChatModule module) {
        this.module = module;
        this.core = this.module.getCore();
    }

    @EventHandler
    public void onChat(@Nonnull final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        String chatFormat = Message.CHAT_FORMAT.getMessage(this.core, false);
        String playerMessage = e.getMessage();

        if (Permission.COLOR_CHAT.has(p, this.core)) {
            playerMessage = textOfRaw(playerMessage);
        }

        chatFormat = chatFormat.replace("{player}", p.getName());
        chatFormat = textOfRaw(chatFormat.replace("{rank}", this.module.getPrefix(p)));
        chatFormat = chatFormat.replace("{message}", playerMessage);
        e.setFormat(chatFormat);
        e.setCancelled(true);
        Bukkit.broadcastMessage(chatFormat);
    }
}
