package net.prisoncore.core.modules.prisonmmo.utils;

import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.prisonmmo.PrisonMMOModule;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.dataservice.UserMMO;
import net.prisoncore.core.utils.messages.Message;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.UUID;

import static net.prisoncore.core.utils.Utils.playSound;

public class ExpQueueWorker {

    private final HashMap<UUID, HashMap<QueueType, Double>> updateQueue;
    private final BukkitTask runningTask;
    private PrisonMMOModule module;
    private final PrisonCore core;

    public ExpQueueWorker(@Nonnull final PrisonMMOModule module) {
        this.updateQueue = new HashMap<>();
        this.module = module;
        this.core = module.getCore();

        this.runningTask = new BukkitRunnable() {
            @Override
            public void run() {
                core.getLog().log("&3Flushing ExpQueueWorker for PrisonMMO Module!");
                updateQueue.forEach((uuid, map) -> {
                    final User user = core.getPlayerDataService().getUser(uuid);
                    final UserMMO mmo = user.getMMO();
                    mmo.setMiningExpIncrease(module.getMiningExpIncrease());
                    mmo.setPlayerExpIncrease(module.getPlayerExpIncrease());
                    map.forEach((type, exp) -> {
                        int increased = 0;
                        switch (type) {
                            case PLAYER: {
                                increased = user.getMMO().addPlayerExp(exp);
                                break;
                            }

                            case MINING: {
                                increased = user.getMMO().addMiningExp(exp);
                                break;
                            }
                        }

                        if (increased > 0) notifyLevelUp(uuid, type, increased);
                    });
                });
                updateQueue.clear();
                core.getLog().log("&3ExpQueueWorker for PrisonMMO Module has been flushed!");
            }
        }.runTaskTimerAsynchronously(module.getCore(), 20, 20 * 5);
    }

    private void notifyLevelUp(@Nonnull final UUID uuid,
                               @Nonnull final QueueType type,
                               final int levelsIncreased) {
        final Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            String msg = Message.MMO_SKILL_INCREASE.getMessage(core, false);
            switch (type) {
                case PLAYER: {
                    msg = msg.replace("{0}", String.valueOf(levelsIncreased));
                    msg = msg.replace("{1}", "Player");
                    break;
                }
                case MINING: {
                    msg = msg.replace("{0}", String.valueOf(levelsIncreased));
                    msg = msg.replace("{1}", "Mining");
                    break;
                }
            }

            p.sendMessage(msg);
            playSound(p, Sound.ENTITY_PLAYER_LEVELUP);
        }
    }

    public void addExpToQueue(@Nonnull final UUID uuid,
                    @Nonnull final QueueType type,
                    final double expToAdd) {
        HashMap<QueueType, Double> map = this.updateQueue.get(uuid);
        final boolean noEntryExists = map == null;
        if (noEntryExists) {
            map = new HashMap<>();
        }

        if (map.get(type) == null) {
            map.putIfAbsent(type, expToAdd);
        } else {
            final double exp = map.get(type);
            map.replace(type, (exp + expToAdd));
        }

        if (noEntryExists) {
            this.updateQueue.putIfAbsent(uuid, map);
        } else this.updateQueue.replace(uuid, map);
    }
}
