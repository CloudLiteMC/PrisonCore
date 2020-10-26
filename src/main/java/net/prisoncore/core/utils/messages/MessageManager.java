package net.prisoncore.core.utils.messages;

import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;

import javax.annotation.Nonnull;
import java.io.File;

import static net.prisoncore.core.utils.config.FileSystem.getJsonData;

public final class MessageManager extends Module {

    private final PrisonCore core;
    private JsonObject messages;

    public MessageManager(@Nonnull final PrisonCore core) {
        this.core = core;
    }

    @Override
    public Module init() {
        this.load();
        return this;
    }

    @Override
    public void reload() {
        this.load();
    }

    private void load() {
        final File file = new File(this.core.getDataFolder(), "messages.json");
        this.messages = getJsonData(file);
    }

    public final JsonObject getMessagesObject() {
        return this.messages;
    }
}
