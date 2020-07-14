package eu.endermite.wanderingvendors.trades.types;

import eu.endermite.wanderingvendors.WanderingVendors;
import me.badbones69.crazycrates.api.CrazyCrates;
import me.badbones69.crazycrates.api.objects.Crate;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

public class CrazyCratesKeyTrade {

    private static final Configuration config = WanderingVendors.getPlugin().getConfig();

    public static ItemStack getItem(String configsection, String type) {

        String cratename = config.getString("trades." + configsection + "." + type + ".crate");
        int amount = config.getInt("trades." + configsection + "." + type + ".amount");

        for (Crate crate : CrazyCrates.getInstance().getCrates()) {
            if (crate.getName().equalsIgnoreCase(cratename)) {
                return crate.getKey(amount);
            }
        }
        return null;
    }


}
