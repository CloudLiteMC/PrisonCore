package net.prisoncore.core.modules.essentials.motd;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.modules.essentials.motd.events.ServerPingEvent;
import net.prisoncore.core.utils.messages.Message;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public final class ServerMotdModule extends Module {

    private final PrisonCore core;

    private ServerPingEvent serverPingEvent;
    private String motdLine1;
    private String motdLine2;

    public ServerMotdModule(@Nonnull final PrisonCore core) {
        this.core = core;
        this.motdLine1 = Message.MOTD_LINE_1.getMessage(this.core, false);
        this.motdLine2 = Message.MOTD_LINE_2.getMessage(this.core, false);
    }


    @Override
    public Module init() {
        this.core.getLog().log("&3Initializing ServerMotdModule. . .");
        this.serverPingEvent = new ServerPingEvent(this.core, this);
        this.core.registerEvent(this.serverPingEvent);
        this.core.getLog().log("&3ServerMotdModule initialized!");
        return this;
    }

    @Override
    public void reload() {
        HandlerList.unregisterAll(this.serverPingEvent);
        this.serverPingEvent = new ServerPingEvent(this.core, this);
        this.core.registerEvent(this.serverPingEvent);
    }

    public final String getMotd() {
        return this.motdLine1 + "\n" + this.motdLine2;
    }
}
