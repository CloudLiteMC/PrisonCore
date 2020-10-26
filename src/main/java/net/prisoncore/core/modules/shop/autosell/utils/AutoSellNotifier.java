package net.prisoncore.core.modules.shop.autosell.utils;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.shop.autosell.AutoSell;
import net.prisoncore.core.utils.messages.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.UUID;

import static net.prisoncore.core.utils.Utils.getPrettyNumber;

public final class AutoSellNotifier {

    private final AutoSell autoSell;
    private final PrisonCore core;
    private final HashMap<UUID, Double> moneyMadeLocal;
    private final HashMap<UUID, BukkitTask> notifiers;
    private final HashMap<UUID, AutoSellReport> lastReport;
    private int notifyRate;

    public AutoSellNotifier(@Nonnull final AutoSell autoSell) {
        this.autoSell = autoSell;
        this.core = this.autoSell.getCore();
        this.moneyMadeLocal = new HashMap<>();
        this.notifiers = new HashMap<>();
        this.lastReport = new HashMap<>();
        this.notifyRate = this.autoSell.getRefreshRate();
    }

    /**
     * Verifies a Player's AutoSell notifier.
     * @param playerId UUID of Player
     */
    public void verifyNotifier(@Nonnull final UUID playerId) {
        final BukkitTask task = this.notifiers.get(playerId);
        if (task == null) {
            this.notifiers.putIfAbsent(playerId, new BukkitRunnable() {
                @Override
                public void run() {
                    final Player player = Bukkit.getPlayer(playerId);
                    if (player == null) {
                        removeNotifier(playerId);
                        return;
                    }

                    final double moneyMade = moneyMadeLocal.get(playerId);
                    if (moneyMade == 0D) {
                        removeNotifier(playerId);
                        return;
                    }

                    String notif = Message.AUTO_SELL_NOTIFICATION.getMessage(core, false);
                    notif = notif.replace("{0}", getPrettyNumber(moneyMade));
                    player.sendActionBar(notif);
                    moneyMadeLocal.replace(playerId, 0D);
                    autoSell.updateReport(playerId, lastReport.get(playerId));
                    lastReport.remove(playerId);
                }
            }.runTaskTimerAsynchronously(this.core, 20 * this.notifyRate, 20 * this.notifyRate));
        }
    }

    /**
     * Adds data to player's AutoSell notifier and AutoSellReport
     * @param uuid od Player
     * @param amountMade money made
     * @param amountSold amount of material sold
     * @param typeSold material sold
     */
    public void addMoneyMade(@Nonnull final UUID uuid, final double amountMade,
                             final int amountSold, @Nonnull final Material typeSold) {
        this.verifyNotifier(uuid);
        this.addMoneyToLocal(uuid, amountMade);
        AutoSellReport report = this.lastReport.get(uuid);
        if (report == null) {
            report = new AutoSellReport();
            this.lastReport.putIfAbsent(uuid, report);
        }
        report.addToReport(typeSold, amountSold, amountMade);
    }

    /**
     * Adds money to a Player's AutoSell notifier
     * @param uuid of Player
     * @param amount of money made
     */
    private void addMoneyToLocal(@Nonnull final UUID uuid, final double amount) {
        Double result = this.moneyMadeLocal.get(uuid);
        if (result == null) {
            this.moneyMadeLocal.putIfAbsent(uuid, amount);
        } else {
            result += amount;
            this.moneyMadeLocal.replace(uuid, result);
        }
    }

    /**
     * Cancels and removes a Player's notifier
     * @param playerId UUID of player to remove
     */
    public void removeNotifier(@Nonnull final UUID playerId) {
        if (this.notifiers.get(playerId) != null) {
            this.notifiers.get(playerId).cancel();
            this.notifiers.remove(playerId);
            this.moneyMadeLocal.remove(playerId);
        }
    }
}
