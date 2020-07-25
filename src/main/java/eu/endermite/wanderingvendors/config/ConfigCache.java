package eu.endermite.wanderingvendors.config;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import java.util.HashMap;

public class ConfigCache {

    private Configuration config = WanderingVendors.getPlugin().getConfig();
    private HashMap<String,MerchantRecipe> merchantTrades = new HashMap<>();
    private HashMap<String, HashMap<Integer, ItemStack>> items = new HashMap<>();
    private boolean randomize;
    private int maxTrades;

    public ConfigCache() {

        randomize = config.getBoolean("options.randomize.enabled");
        maxTrades = config.getInt("options.randomize.max-trades");

    }

    public HashMap<String,MerchantRecipe> getMerchantTrades() {
        return merchantTrades;
    }
    public HashMap<String, HashMap<Integer, ItemStack>> getItems() {return items;}
    public boolean isRandomizeEnabled() {return randomize;}
    public int getMaxTrades() {return maxTrades;}
    public boolean addTrade(String id, MerchantRecipe merchantrecipe) {
        try {
            merchantTrades.put(id, merchantrecipe);
            return true;
        } catch (NullPointerException e) {
            WanderingVendors.getPlugin().getLogger().severe("Failed to add trade "+merchantrecipe.toString());
            return false;
        }
    }
    public void editTrade(String id, MerchantRecipe newRecipe) {
        merchantTrades.replace(id, newRecipe);
    }
    public void deleteTrade(String id) {
        merchantTrades.remove(id);
    }


}
