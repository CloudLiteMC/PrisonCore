package net.prisoncore.core.utils.dataservice;

import net.prisoncore.core.utils.exceptions.ValueNotSetException;
import org.bson.Document;

import javax.annotation.Nonnull;

public class UserMMO {

    private final Document playerMmo;
    private final Document miningMmo;

    private double playerExpIncrease = 0D;
    private double miningExpIncrease = 0D;

    public UserMMO(@Nonnull final Document object) {
        this.playerMmo = ((Document) object.get("player"));
        this.miningMmo = ((Document) object.get("mining"));
    }

    public void setPlayerExpIncrease(final double increase) {
        this.playerExpIncrease = increase;
    }

    public final int getPlayerLevel() {
        if (this.playerExpIncrease == 0D) throw new ValueNotSetException("PlayerMMO playerExpIncrease value not set when trying to get UserMMO Player level");
        return ((int) (this.getPlayerExp() / this.playerExpIncrease)) + 1;
    }

    public final double getPlayerExp() {
        return this.playerMmo.getDouble("currentExp");
    }

    public final int addPlayerExp(final double exp) {
        final int pastLevel = this.getPlayerLevel();
        this.playerMmo.replace("currentExp", (this.getPlayerExp() + exp));
        return (this.getPlayerLevel() - pastLevel);
    }

    public void setMiningExpIncrease(final double increase) {
        this.miningExpIncrease = increase;
    }

    public final int getMiningLevel() {
        if (this.miningExpIncrease == 0D) throw new ValueNotSetException("PlayerMMO miningExpIncrease value not set when trying to get UserMMO Mining level");
        return ((int) (this.getMiningExp() / this.miningExpIncrease)) + 1;
    }

    public final double getMiningExp() {
        return this.miningMmo.getDouble("currentExp");
    }

    public final int addMiningExp(final double exp) {
        final int pastLevel = this.getMiningLevel();
        this.miningMmo.replace("currentExp", (this.getMiningExp() + exp));
        return (this.getMiningLevel() - pastLevel);
    }

    public final Document getDatabaseDocument() {
        final Document doc = new Document();
        doc.append("player", this.playerMmo);
        doc.append("mining", this.miningMmo);
        return doc;
    }
}
