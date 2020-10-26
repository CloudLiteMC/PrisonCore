package net.prisoncore.core.modules.shop.autosell.events;

import net.prisoncore.core.modules.shop.ShopModule;
import net.prisoncore.core.modules.shop.autosell.AutoSell;
import net.prisoncore.core.modules.shop.autosell.utils.AutoSellNotifier;
import net.prisoncore.core.utils.dataservice.User;
import net.prisoncore.core.utils.fortune.utils.AutoSellEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class PlayerBreaksBlock implements Listener {

    private final ShopModule shopModule;
    private final AutoSellNotifier notifier;

    public PlayerBreaksBlock(@Nonnull final AutoSell autoSell) {
        this.shopModule = autoSell.getShopModule();
        this.notifier = new AutoSellNotifier(autoSell);
    }

    @EventHandler (
            priority = EventPriority.HIGHEST
    )
    public void onAutoSell(@Nonnull final AutoSellEvent e) {
        final User user = e.getUser();
        final Material broken = e.getBrokenMaterial();
        final int amount = e.getAmountBroken();

        final double pricePer = this.shopModule.getPriceForItem(broken);
        if (pricePer <= 0D) {
            user.getPlayer().getInventory().addItem(new ItemStack(broken, amount));
        } else {
            final double total = (pricePer * amount);
            user.addBalance(total);
            this.notifier.addMoneyMade(user.getUuid(), total, amount, broken);
        }
    }
}
