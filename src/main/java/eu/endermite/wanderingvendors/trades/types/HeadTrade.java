package eu.endermite.wanderingvendors.trades.types;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class HeadTrade {

    private static final Configuration config = WanderingVendors.getPlugin().getConfig();

    public static ItemStack getHead(String configsection, String type) {

        int amount = config.getInt("trades."+configsection+"."+type+".amount");

        ItemStack result = new ItemStack(Material.PLAYER_HEAD, amount);

        ItemMeta resultmeta = result.getItemMeta();

        try {
            String name = config.getString("trades."+configsection+"."+type+".name");
            resultmeta.setDisplayName(name);
        } catch (NullPointerException ignored) {}
        if (type.equalsIgnoreCase("result")) {
            try {
                List<String> lore = config.getStringList("trades."+configsection+"."+type+".lore");
                resultmeta.setLore(lore);
            } catch (NullPointerException ignored) {}
        }

        try {
            resultmeta.setCustomModelData(config.getInt("trades."+configsection+"."+type+".custommodeldata"));
        } catch (NullPointerException ignored) {}

        result.setItemMeta(resultmeta);

        SkullMeta skullmeta = (SkullMeta) result.getItemMeta().clone();
        try {
            String headowner = config.getString("trades."+configsection+".result.head");
            skullmeta.setOwningPlayer(Bukkit.getOfflinePlayer(headowner));
            result.setItemMeta(skullmeta);
        } catch (NullPointerException e) {
            WanderingVendors.getPlugin().getLogger().severe("Could not find player.");
        }

        try {
            for (String enchdata : config.getStringList("trades." + configsection + "." + type + ".enchants")) {
                String[] ench = enchdata.split(":");
                result.addEnchantment(Enchantment.getByName(ench[0]), Integer.parseInt(ench[1]));
            }
        } catch (NullPointerException ignored) {}

        return result;
    }

}
