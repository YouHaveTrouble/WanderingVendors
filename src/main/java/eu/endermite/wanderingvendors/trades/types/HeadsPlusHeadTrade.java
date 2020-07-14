package eu.endermite.wanderingvendors.trades.types;

import eu.endermite.wanderingvendors.WanderingVendors;
import io.github.thatsmusic99.headsplus.HeadsPlus;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

public class HeadsPlusHeadTrade {

    private static final Configuration config = WanderingVendors.getPlugin().getConfig();

    public static ItemStack getHead(String configsection, String type) {

        String headname = config.getString("trades." + configsection + "." + type + ".customhead");
        int amount = config.getInt("trades." + configsection + "." + type + ".amount");

        ItemStack head = HeadsPlus.getInstance().getHeadsXConfig().getSkull(headname);
        head.setAmount(amount);

        return head;
    }

}
