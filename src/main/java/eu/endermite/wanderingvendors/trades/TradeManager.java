package eu.endermite.wanderingvendors.trades;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TradeManager {

    private static Configuration config = WanderingVendors.getPlugin().getConfig();


    private static boolean checkState(String configstring) {
        if (config.getString(configstring).equalsIgnoreCase("none")) {
            return false;
        } else if (config.getString(configstring).equalsIgnoreCase("item")) {
            return true;
        } else {
            return false;
        }
    }

    public static ItemStack parseResult(String configsection) {
        if (checkState("trades."+configsection+".result.type")) {

            try {
                String material = config.getString("trades."+configsection+".result.material");
                int amount = config.getInt("trades."+configsection+".result.amount");

                ItemStack result = new ItemStack(Material.getMaterial(material), amount);

                String name = config.getString("trades."+configsection+".result.name");

                ItemMeta resultmeta = result.getItemMeta();

                try {
                    resultmeta.setLocalizedName(name);
                    resultmeta.setDisplayName(name);
                } catch (NullPointerException ignored) {}
                try {
                    List<String> lore = config.getStringList("trades."+configsection+".result.lore");
                    resultmeta.setLore(lore);
                } catch (NullPointerException ignored) {}
                try {
                    resultmeta.setCustomModelData(config.getInt("trades."+configsection+".result.custommodeldata"));
                } catch (NullPointerException ignored) {}


                result.setItemMeta(resultmeta);

                for (String enchandlvl : config.getStringList("trades."+configsection+".result.enchants")) {

                    String[] enchinfo = enchandlvl.split(":");
                    try {
                        result.addUnsafeEnchantment(Enchantment.getByName(enchinfo[0]), Integer.parseInt(enchinfo[1]));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                return result;

            } catch(NullPointerException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public static ItemStack parseIngridient(String configsection, int number) {
        if (checkState("trades."+configsection+".ingridient"+number+".type")) {

            try {
                String material = config.getString("trades."+configsection+".ingridient"+number+".material");
                int amount = config.getInt("trades."+configsection+".ingridient"+number+".amount");

                ItemStack result = new ItemStack(Material.getMaterial(material), amount);

                String name = config.getString("trades."+configsection+".ingridient"+number+".name");

                ItemMeta resultmeta = result.getItemMeta();

                resultmeta.setLocalizedName(name);
                resultmeta.setDisplayName(name);
                result.setItemMeta(resultmeta);

                for (String enchandlvl : config.getStringList("trades."+configsection+".ingridient"+number+".enchants")) {

                    String[] enchinfo = enchandlvl.split(":");
                    try {
                        result.addUnsafeEnchantment(Enchantment.getByName(enchinfo[0]), Integer.parseInt(enchinfo[1]));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                return result;

            } catch(NullPointerException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    public static MerchantRecipe createMerchantRecipe(String configsection) {

        if (parseResult(configsection) == null) {
            return null;
        }

        ItemStack result = parseResult(configsection);
        int maxUses = config.getInt("trades."+configsection+".maxuses");

        MerchantRecipe recipe = null;
        try {
            recipe = new MerchantRecipe(result, maxUses);

        } catch (NullPointerException e) {
            WanderingVendors.getPlugin().getLogger().severe("Could not load result for recipe "+configsection);
        }
        assert recipe != null;

        if (parseIngridient(configsection, 1) != null) {

            recipe.addIngredient(parseIngridient(configsection, 1));
        }
        if (parseIngridient(configsection, 2) != null) {
            recipe.addIngredient(parseIngridient(configsection, 2));
        }
        return recipe;




    }

}
