package eu.endermite.wanderingvendors.trades;

import eu.endermite.wanderingvendors.WanderingVendors;
import eu.endermite.wanderingvendors.trades.types.*;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;


public class TradeManager {

    private static Configuration config = WanderingVendors.getPlugin().getConfig();

    public static MerchantRecipe createMerchantRecipe(String configsection) {
        String resultstring = "";
        try {
            resultstring = config.getString("trades."+configsection+".result.type") ;
        } catch (NullPointerException e) {
            return null;
        }

        ItemStack result;

        switch (resultstring) {
            case ("item"):
                result = ItemTrade.getItem(configsection, "result");
                break;
            case ("head"):
                result = HeadTrade.getItem(configsection, "result");
                break;
            case ("crazycrateskey"):
                if (WanderingVendors.getPlugin().getServer().getPluginManager().getPlugin("CrazyCrates") != null) {
                    result = CrazyCratesKeyTrade.getItem(configsection, "result");
                } else {
                    WanderingVendors.getPlugin().getLogger().severe("CrazyCrates is required for crazycrateskey trade");
                    return null;
                }
                break;
            case ("headsplus"):
                if (WanderingVendors.getPlugin().getServer().getPluginManager().getPlugin("HeadsPlus") != null) {
                    result = HeadsPlusHeadTrade.getItem(configsection, "result");
                } else {
                    WanderingVendors.getPlugin().getLogger().severe("CrazyCrates is required for crazycrateskey trade");
                    return null;
                }
                break;
            case ("wildtools"):
                if (WanderingVendors.getPlugin().getServer().getPluginManager().getPlugin("WildTools") != null) {
                    result = WildToolsTrade.getItem(configsection, "result");
                } else {
                    WanderingVendors.getPlugin().getLogger().severe("WildTools is required for wildtools trade");
                    return null;
                }
                break;
            default:
                return null;
        }

        int maxUses = config.getInt("trades."+configsection+".maxuses");
        MerchantRecipe recipe = null;
        try {
            recipe = new MerchantRecipe(result, maxUses);
        } catch (NullPointerException e) {
            return null;
        }

        for (int i = 1; i <= 2; i++) {

            String ingstring = "";
            try {
                ingstring = config.getString("trades."+configsection+".ingridient"+i+".type") ;
            } catch (NullPointerException e) {
                return null;
            }

            ItemStack ing;
            switch (ingstring) {
                case ("item"):
                    ing = ItemTrade.getItem(configsection, "ingridient"+i);
                    break;
                case ("head"):
                    ing = HeadTrade.getItem(configsection, "ingridient"+i);
                    break;
                case ("crazycrateskey"):
                    if (WanderingVendors.getPlugin().getServer().getPluginManager().getPlugin("CrazyCrates") != null) {
                        ing = CrazyCratesKeyTrade.getItem(configsection, "ingridient"+i);
                    } else {
                        WanderingVendors.getPlugin().getLogger().severe("CrazyCrates is required for crazycrateskey trade");
                        return null;
                    }
                    break;
                case ("headsplus"):
                    if (WanderingVendors.getPlugin().getServer().getPluginManager().getPlugin("HeadsPlus") != null) {
                        ing = HeadsPlusHeadTrade.getItem(configsection, "ingridient"+i);
                    } else {
                        WanderingVendors.getPlugin().getLogger().severe("HeadsPlus is required for headsplus trade");
                        return null;
                    }
                    break;
                case ("wildtools"):
                    if (WanderingVendors.getPlugin().getServer().getPluginManager().getPlugin("WildTools") != null) {
                        ing = WildToolsTrade.getItem(configsection, "ingridient"+i);
                    } else {
                        WanderingVendors.getPlugin().getLogger().severe("WildTools is required for wildtools trade");
                        return null;
                    }
                    break;
                default:
                    ing = new ItemStack(Material.AIR, 1);
            }
            recipe.addIngredient(ing);
        }

        return recipe;

    }

}
