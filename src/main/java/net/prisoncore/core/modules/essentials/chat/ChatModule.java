package net.prisoncore.core.modules.essentials.chat;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.essentials.chat.events.ChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;

import javax.annotation.Nonnull;

import static net.prisoncore.core.utils.Utils.textOfRaw;

public class ChatModule extends Module {

    private final PrisonCore core;
    private ChatEvent chatEvent;
    private Chat chat;

    public ChatModule(@Nonnull final PrisonCore core) {
        this.core = core;
    }

    @Override
    public Module init() {
        this.loadVaultChat();

        this.chatEvent = new ChatEvent(this);
        this.core.registerEvent(this.chatEvent);
        return this;
    }

    @Override
    public void reload() {

        HandlerList.unregisterAll(this.chatEvent);
        this.chatEvent = new ChatEvent(this);
        this.core.registerEvent(this.chatEvent);
    }

    /**
     * Loads the Vault Permissions service
     */
    private void loadVaultChat() {
        final RegisteredServiceProvider<Chat> rsp = this.core.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (rsp == null) {
            throw new RuntimeException("Could not load VaultChat manager!");
        }
        chat = rsp.getProvider();
    }

    public final PrisonCore getCore() {
        return this.core;
    }

    public String getPrefix(@Nonnull final Player p) {
        return textOfRaw(this.chat.getPlayerPrefix(p));
    }
}
