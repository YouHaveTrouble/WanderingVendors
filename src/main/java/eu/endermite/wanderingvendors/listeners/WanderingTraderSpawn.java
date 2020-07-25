package eu.endermite.wanderingvendors.listeners;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WanderingTraderSpawn implements Listener {

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onEntitySpawn(org.bukkit.event.entity.CreatureSpawnEvent event) {
        if(event.getEntity() instanceof WanderingTrader) {
            WanderingTrader trader = (WanderingTrader) event.getEntity();

            if (WanderingVendors.getConfigCache().isRandomizeEnabled()) {
                int maxTrades = WanderingVendors.getConfigCache().getMaxTrades();

                List<MerchantRecipe> cache = new ArrayList(WanderingVendors.getConfigCache().getMerchantTrades().values());
                List<MerchantRecipe> randomized = new ArrayList<>();
                Random random = new Random();

                for (int i = 1; i<= maxTrades; i++) {
                    try {
                        int r = random.nextInt(cache.size());
                        randomized.add(cache.get(r));

                        cache.remove(r);

                    } catch (Exception e ) {
                        break;
                    }
                }
                trader.setRecipes(randomized);
            } else {

                List<MerchantRecipe> reclist = new ArrayList<>();

                for (MerchantRecipe rec : WanderingVendors.getConfigCache().getMerchantTrades().values()) {
                    reclist.add(rec);
                }
                if (reclist.isEmpty()) {
                    return;
                }
                trader.setRecipes(reclist);
            }
        }
    }

}
