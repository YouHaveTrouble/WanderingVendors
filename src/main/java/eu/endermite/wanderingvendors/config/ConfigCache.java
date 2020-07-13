package eu.endermite.wanderingvendors.config;

import eu.endermite.wanderingvendors.WanderingVendors;
import eu.endermite.wanderingvendors.trades.TradeManager;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.MerchantRecipe;
import java.util.ArrayList;
import java.util.List;

public class ConfigCache {

    private Configuration config = WanderingVendors.getPlugin().getConfig();
    private List<MerchantRecipe> merchantTrades = new ArrayList<>();

    public ConfigCache() {
        for (String s : config.getConfigurationSection("trades").getKeys(false)) {

            MerchantRecipe newTrade = TradeManager.createMerchantRecipe(s);
            merchantTrades.add(newTrade);

        }
    }

    public List<MerchantRecipe> getMerchantTrades() {
        return merchantTrades;
    }

}
