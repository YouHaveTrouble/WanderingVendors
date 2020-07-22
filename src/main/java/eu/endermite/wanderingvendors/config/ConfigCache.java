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
    private boolean randomize;
    private int maxTrades;

    public ConfigCache() {

        randomize = config.getBoolean("options.randomize.enabled");
        maxTrades = config.getInt("options.randomize.max-trades");

        for (String s : config.getConfigurationSection("trades").getKeys(false)) {
            try {
                MerchantRecipe newTrade = TradeManager.createMerchantRecipe(s);
                HashMap<Integer, ItemStack> index = new HashMap<>();
                index.put(0,newTrade.getResult());
                index.put(1, newTrade.getIngredients().get(0));
                index.put(2, newTrade.getIngredients().get(1));
                items.put(s, index);
                merchantTrades.add(newTrade);

            } catch (NullPointerException e) {
                WanderingVendors.getPlugin().getLogger().severe("Failed to load trade "+s);
            }

        }
    }

    public List<MerchantRecipe> getMerchantTrades() {
        return merchantTrades;
    }
    public HashMap<String, HashMap<Integer, ItemStack>> getItems() {return items;}
    public boolean isRandomizeEnabled() {return randomize;}
    public int getMaxTrades() {return maxTrades;}
    public boolean addTrade(MerchantRecipe merchantrecipe) {
        try {
            merchantTrades.add(merchantrecipe);
            return true;
        } catch (NullPointerException e) {
            WanderingVendors.getPlugin().getLogger().severe("Failed to add trade "+merchantrecipe.toString());
            return false;
        }
    }
    public void editTrade(int id, MerchantRecipe newRecipe) {
        merchantTrades.set(id, newRecipe);
    }


}
