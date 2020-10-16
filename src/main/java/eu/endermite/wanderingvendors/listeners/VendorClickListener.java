package eu.endermite.wanderingvendors.listeners;

import eu.endermite.wanderingvendors.gui.TradeListMain;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.MerchantRecipe;
import java.util.HashMap;

public class VendorClickListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onVendorInteract(org.bukkit.event.player.PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("wanderingvendors.edittrades"))
            return;

        if (!player.isSneaking())
            return;

        HashMap<String, MerchantRecipe> trades = new HashMap<>();

        int count = 0;

        if (event.getRightClicked() instanceof Villager) {
            Villager villager = (Villager) event.getRightClicked();
            for (MerchantRecipe recipe : villager.getRecipes()) {
                trades.put(String.valueOf(count++), recipe);
            }
        } else if (event.getRightClicked() instanceof WanderingTrader) {
            WanderingTrader trader = (WanderingTrader) event.getRightClicked();
            for (MerchantRecipe recipe : trader.getRecipes()) {
                trades.put(String.valueOf(count++), recipe);
            }
        }

        event.setCancelled(true);
        TradeListMain.openGui(player, 0, trades, "Edit merchant "+event.getRightClicked().getUniqueId(), event.getRightClicked().getUniqueId().toString());
    }

}
