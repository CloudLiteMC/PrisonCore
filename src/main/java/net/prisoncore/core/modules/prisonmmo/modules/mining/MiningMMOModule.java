package net.prisoncore.core.modules.prisonmmo.modules.mining;

import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.prisonmmo.PrisonMMOModule;
import net.prisoncore.core.modules.prisonmmo.modules.mining.events.BlockBreak;
import net.prisoncore.core.utils.config.Config;
import net.prisoncore.core.utils.exceptions.MaterialNotFoundException;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.UUID;

import static net.prisoncore.core.utils.config.FileSystem.getJsonData;

public class MiningMMOModule {

    private final PrisonMMOModule module;
    private final PrisonCore core;
    private final HashMap<Material, Double> miningExpPerBlock;

    public MiningMMOModule(@Nonnull final PrisonMMOModule module) {
        this.module = module;
        this.core = this.module.getCore();

        this.core.getLog().log("&3Loading PrisonMMO Module: &cMINING");
        this.miningExpPerBlock = new HashMap<>();
        this.loadBlocks();

        this.core.registerEvent(new BlockBreak(this));

        this.core.getLog().log("&3PrisonMMO Module: &cMINING&a Loaded!");
    }

    private void loadBlocks() {
        final JsonObject obj = getJsonData(Config.MMO, this.core);

        obj.getAsJsonObject("mining").getAsJsonArray("blocks").forEach((element) -> {
            final String[] s = element.getAsString().split(":");
            final Material mat = Material.getMaterial(s[0]);
            final double d = Double.parseDouble(s[1]);
            if (mat == null) throw new MaterialNotFoundException("Material was invalid when loading exp blocks for mining mmo: " + s[0]);
            this.miningExpPerBlock.putIfAbsent(mat, d);
        });
        this.core.getLog().log("&3Successfully loaded &e" + this.miningExpPerBlock.size() + "&3 block with MininMMO EXP!");
    }

    public final double getExpForMaterial(@Nonnull final Material mat) {
        if (this.miningExpPerBlock.get(mat) == null) {
            return -1D;
        } return this.miningExpPerBlock.get(mat);
    }

    public void addExp(@Nonnull final UUID uuid, final double exp) {
        this.module.addMiningExp(uuid, exp);
    }
}
