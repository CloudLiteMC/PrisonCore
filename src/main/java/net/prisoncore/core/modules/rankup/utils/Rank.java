package net.prisoncore.core.modules.rankup.utils;

import com.google.gson.JsonObject;
import net.prisoncore.core.utils.exceptions.MaterialNotFoundException;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import static net.prisoncore.core.utils.Utils.textOfRaw;

public final class Rank {

    private final String thisRankName;
    private final String nextRankName;
    private final double rankupCost;
    private final String playerRankupMessage;
    private final boolean doBroadcast;
    private final String broadcastMessage;
    private final List<String> commands;
    public final Material material;

    public Rank(@Nonnull final JsonObject rankObject) {
        this.thisRankName = rankObject.get("thisRankName").getAsString();
        this.nextRankName = rankObject.get("nextRankName").getAsString();
        this.rankupCost = rankObject.get("rankupCost").getAsDouble();
        this.playerRankupMessage = textOfRaw(rankObject.get("message").getAsString().replace("{0}", this.getNextRankName()));
        this.doBroadcast = rankObject.get("broadcast").getAsBoolean();
        this.broadcastMessage = textOfRaw(rankObject.get("broadcastMessage").getAsString().replace("{1}", this.getNextRankName()));
        this.commands = new ArrayList<>();
        rankObject.get("commands").getAsJsonArray().forEach((element) -> this.commands.add(element.getAsString()));
        String matString = rankObject.get("material").getAsString();
        this.material = Material.getMaterial(matString);
        if (this.material == null) throw new MaterialNotFoundException("Could not find material for: " + matString);
    }

    public final String getThisRankName() {
        return this.thisRankName;
    }

    public final String getNextRankName() {
        return this.nextRankName;
    }

    public final double getRankupCost() {
        return this.rankupCost;
    }

    public final String getPlayerRankupMessage() {
        return this.playerRankupMessage;
    }

    public final boolean isDoBroadcast() {
        return this.doBroadcast;
    }

    public final String getBroadcastMessage(@Nonnull final Player player) {
        return this.broadcastMessage.replace("{0}", player.getName());
    }

    public final List<String> getCommands() {
        return this.commands;
    }

    public final Material getMaterial() {
        return this.material;
    }
}
