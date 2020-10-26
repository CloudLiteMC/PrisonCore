package net.prisoncore.core.utils.fortune;

import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.Module;
import net.prisoncore.core.utils.config.Config;
import net.prisoncore.core.utils.exceptions.MaterialNotFoundException;
import net.prisoncore.core.utils.fortune.events.FortuneBlockBreak;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static net.prisoncore.core.utils.config.FileSystem.getJsonData;

public final class FortuneModule extends Module {

    private final PrisonCore core;

    private List<Material> blacklist;

    public FortuneModule(@Nonnull final PrisonCore core) {
        this.core = core;
    }


    @Override
    public Module init() {
        this.loadBlacklist();
        this.core.registerEvent(new FortuneBlockBreak(this));

        return this;
    }

    @Override
    public void reload() {
        this.loadBlacklist();
    }

    private void loadBlacklist() {
        this.blacklist = new ArrayList<>();

        final JsonObject obj = getJsonData(Config.FORTUNE, this.core);
        obj.getAsJsonArray("blacklistBlocks").forEach((ele) -> {
            final Material mat = Material.getMaterial(ele.getAsString());
            if (mat == null) throw new MaterialNotFoundException("Material not found when loading fortune blacklist: " + ele.getAsString());
            this.blacklist.add(mat);
        });
    }

    /**
     * Checks ifa material is blacklisted
     * @param mat Material to check
     * @return true if material is on the blacklist
     */
    public boolean isBlacklisted(@Nonnull final Material mat) {
        return this.blacklist.contains(mat);
    }

    public final PrisonCore getCore() {
        return this.core;
    }
}
