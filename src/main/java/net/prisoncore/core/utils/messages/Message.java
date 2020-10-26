package net.prisoncore.core.utils.messages;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.prisoncore.core.PrisonCore;
import net.prisoncore.core.modules.ModuleType;
import net.prisoncore.core.utils.Utils;
import net.prisoncore.core.utils.exceptions.JsonDataNotFoundException;
import net.prisoncore.core.utils.exceptions.MessageNotFoundException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.annotation.Nonnull;

import static net.prisoncore.core.utils.Utils.textOfRaw;

public enum Message {

    PREFIX("general.prefix"),
    INVALID_USAGE("general.invalidUsage"),
    NO_PERMISSION("general.noPermission"),
    CONSOLE_NOT_ALLOWED("general.consoleSenderNotAllowed"),
    PLAYER_NOT_ONLINE("general.playerNotOnline"),
    INVALID_INTEGER("general.invalidNumberProvided"),

    MOTD_LINE_1("motd.line1"),
    MOTD_LINE_2("motd.line2"),

    BALANCE_OTHER_PLAYER("economy.balanceOtherPlayer"),
    BALANCE_SELF_PLAYER("economy.balanceSelfPlayer"),
    PAYER_MESSAGE("economy.payerMessage"),
    PAYER_RECEIVER_MESSAGE("economy.payerReceiver"),
    ADMIN_MONEY_SENDER_GIVE("economy.adminMoneySenderGive"),
    ADMIN_MONEY_RECEIVER_GIVE("economy.adminMoneyReceiverGive"),
    ADMIN_MONEY_SENDER_TAKE("economy.adminMoneySenderTake"),
    ADMIN_MONEY_RECEIVER_TAKE("economy.adminMoneyReceiverTake"),
    ADMIN_MONEY_SENDER_SET("economy.adminMoneySenderSet"),
    ADMIN_MONEY_RECEIVER_SET("economy.adminMoneyReceiverSet"),

    CHAT_FORMAT("chat.format"),

    MMO_SKILL_INCREASE("prisonMMO.skillIncrease"),

    AUTO_SELL_TOGGLE("autoSell.toggle"),
    NO_REPORT_EXISTS("autoSell.noReportExists"),
    AUTO_SELL_ENABLE_DEFAULT("autoSell.defaultEnable"),
    AUTO_SELL_NOTIFICATION("autoSell.autoSellNotification"),

    TARGET_HEALED_MESSAGE("heal.target"),
    PLAYER_ON_HEAL_TIMER("heal.timer"),
    SENDER_HEALED_MESSAGE("heal.sender"),

    TARGET_FEED_MESSAGE("feed.target"),
    PLAYER_ON_FEED_TIMER("feed.timer"),
    SENDER_FEED_MESSAGE("feed.sender"),

    RANKUP_MAX_RANK("rankup.maxRank"),
    RANK_NOT_ENOUGH_MONEY("rankup.insufficientMoney"),

    TARGET_GAMEMODE_CHANGED("gameMode.targetGameModeChanged"),
    SENDER_CONFIRM_TARGET_GAMEMODE_CHANGED("gameMode.senderGameModeConfirm"),

    TARGET_INVENTORY_CLEARED("clearInventory.targetInventoryCleared"),
    TARGET_INVENTORY_REQUEST_CANCELLED("clearInventory.clearInventoryRequestCancelled"),
    SENDER_INVENTORY_CLEAR_COMPLETE("clearInventory.senderClearInventoryConfirm");

    private final String path;

    Message(@Nonnull final String path) {
        this.path = path;
    }

    public final String getMessage(@Nonnull final PrisonCore core, final boolean withPrefix) {
        final JsonObject messages = ((MessageManager) core.getModule(ModuleType.MESSAGES)).getMessagesObject();
        JsonElement ele = null;
        if (messages == null) throw new JsonDataNotFoundException("Data not found when trying to find message");
        for (final String field : this.path.split("\\.")) {
            if (ele == null) {
                ele = messages.get(field);
            } else {
                JsonObject object = ele.getAsJsonObject();
                ele = object.get(field);
            }
        }
        if (ele == null) throw new MessageNotFoundException("Message not found: " + this.path);
        if (withPrefix) {
            return textOfRaw(PREFIX.getMessage(core, false) + " &r" + ele.getAsString());
        } else return textOfRaw(ele.getAsString());
    }

    public void sendMessage(@Nonnull final Player p, @Nonnull final PrisonCore core) {
        p.sendMessage(this.getMessage(core, true));
    }

    public void sendMessage(@Nonnull final ConsoleCommandSender c, @Nonnull final PrisonCore core) {
        c.sendMessage(this.getMessage(core, true));
    }
}
