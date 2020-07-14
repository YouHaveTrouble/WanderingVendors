package eu.endermite.wanderingvendors.trades.types;

import com.bgsoftware.wildtools.api.WildToolsAPI;
import com.bgsoftware.wildtools.api.objects.tools.Tool;
import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

public class WildToolsTrade {

    private static final Configuration config = WanderingVendors.getPlugin().getConfig();

    public static ItemStack getItem(String configsection, String type) {

        String toolname = config.getString("trades." + configsection + "." + type + ".tool");

        int uses = 0;

        try {
            uses = config.getInt("trades." + configsection + "." + type + ".uses");
        } catch (NullPointerException ignored) {}


        for (Tool tool : WildToolsAPI.getWildTools().getToolsManager().getTools()) {
            if (tool.getName().equalsIgnoreCase(toolname)) {
                if (uses > 0) {
                    return tool.getFormattedItemStack(uses);
                } else {
                    return tool.getFormattedItemStack();
                }
            }
        }
        return null;

    }

}
