package eu.endermite.wanderingvendors.config;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import java.io.File;
import java.io.IOException;

public class CreatorTradesConfig {

    private static final WanderingVendors plugin = WanderingVendors.getPlugin();
    private static FileConfiguration creatorConfig;

    public static void saveTrade(String id, MerchantRecipe recipe) {

        creatorConfig.set("recipes."+id+".uses", recipe.getMaxUses());
        creatorConfig.set("recipes."+id+".result", recipe.getResult());
        if (recipe.getIngredients().get(0) != null) {
            creatorConfig.set("recipes."+id+".ingridient1", recipe.getIngredients().get(0));
        }
        if (recipe.getIngredients().size() == 2 && recipe.getIngredients().get(1) != null) {
            creatorConfig.set("recipes."+id+".ingridient2", recipe.getIngredients().get(1));
        }

        try {
            File creatorConfigFile = new File(plugin.getDataFolder(), "creatortrades.yml");
            creatorConfig.save(creatorConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTrade(String id) {

        creatorConfig.set("recipes."+id, null);
        try {
            File creatorConfigFile = new File(plugin.getDataFolder(), "creatortrades.yml");
            creatorConfig.save(creatorConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MerchantRecipe loadTrade(String id) {

        Integer uses = creatorConfig.getInt("recipes."+id+".uses");
        ItemStack result = creatorConfig.getItemStack("recipes."+id+".result");
        MerchantRecipe recipe = new MerchantRecipe(result, uses);

        if (creatorConfig.getItemStack("recipes."+id+".ingridient1") != null) {
            ItemStack ingridient1 = creatorConfig.getItemStack("recipes."+id+".ingridient1");
            recipe.addIngredient(ingridient1);
        }
        if (creatorConfig.getItemStack("recipes."+id+".ingridient2") != null) {
            ItemStack ingridient2 = creatorConfig.getItemStack("recipes."+id+".ingridient2");
            recipe.addIngredient(ingridient2);
        }
        return recipe;
    }

    public static void setupCreatorTrades() {

        File creatorConfigFile = new File(plugin.getDataFolder(), "creatortrades.yml");
        creatorConfig = new YamlConfiguration();

        if (!creatorConfigFile.exists()) {
            creatorConfigFile.getParentFile().mkdirs();
            plugin.saveResource("creatortrades.yml", false);
        }
        try {
            creatorConfig.load(creatorConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (String id : creatorConfig.getConfigurationSection("recipes").getKeys(false)) {
                MerchantRecipe trade = loadTrade(id);
                WanderingVendors.getConfigCache().addTrade(id, trade);
            }
        } catch (NullPointerException ignored) {}
    }

}
