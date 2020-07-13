package eu.endermite.wanderingvendors.config;

import eu.endermite.wanderingvendors.WanderingVendors;
import eu.endermite.wanderingvendors.trades.TradeManager;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigCache {

    private Configuration config = WanderingVendors.getPlugin().getConfig();
    private List<MerchantRecipe> merchantTrades = new ArrayList<>();
    private HashMap<String, HashMap<Integer, ItemStack>> items = new HashMap<>();

    public ConfigCache() {
        for (String s : config.getConfigurationSection("trades").getKeys(false)) {

            try {
                MerchantRecipe newTrade = TradeManager.createMerchantRecipe(s);
                merchantTrades.add(newTrade);
                HashMap<Integer, ItemStack> index = new HashMap<>();
                index.put(0,newTrade.getResult());
                index.put(1, newTrade.getIngredients().get(0));
                index.put(2, newTrade.getIngredients().get(1));
                items.put(s, index);

            } catch (NullPointerException e) {
                WanderingVendors.getPlugin().getLogger().severe("Failed to load trade "+s);
            }

        }
    }

    public List<MerchantRecipe> getMerchantTrades() {
        return merchantTrades;
    }
    public HashMap<String, HashMap<Integer, ItemStack>> getItems() {return items;}


}
