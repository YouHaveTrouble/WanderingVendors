package eu.endermite.wanderingvendors.listeners;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class WanderingTraderSpawn implements Listener {

    @EventHandler(priority = EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntitySpawn(org.bukkit.event.entity.CreatureSpawnEvent event) {
        if(event.getEntity() instanceof WanderingTrader) {
            WanderingTrader trader = (WanderingTrader) event.getEntity();

            trader.setRecipes(WanderingVendors.getConfigCache().getMerchantTrades());


        }
    }

}
