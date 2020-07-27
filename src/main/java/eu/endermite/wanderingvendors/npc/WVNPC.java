package eu.endermite.wanderingvendors.npc;

import jdk.nashorn.internal.objects.annotations.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.MerchantRecipe;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WVNPC {


    private Location location;
    private String customName;
    private HashMap<String, MerchantRecipe> trademap = new HashMap<>();

    WVNPC(String id, Location loc, HashMap<String, MerchantRecipe> trades) {

        customName = null;
        location = loc;
        trademap = trades;

    }

    // NPC Data

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location loc) {
        location = loc;
    }

    public String getCustomName() {
        return ChatColor.translateAlternateColorCodes('&',customName);
    }

    public void setCustomName(String name) {
        customName = name;
    }

    public boolean hasCustomName() {
        return customName != null;
    }


    // NPC Trades

    public HashMap<String, MerchantRecipe> getTradelist() {
        return trademap;
    }

    public void setTrades(List<MerchantRecipe> list) {
        trademap.clear();
        if (!list.isEmpty()) {
            for (MerchantRecipe recipe : list) {
                trademap.put(UUID.randomUUID().toString(), recipe);
            }
        }
    }

    public void addTrade(String id, MerchantRecipe trade) {
        trademap.put(id, trade);
    }

    public void editTrade(String id, MerchantRecipe newtrade) {
        trademap.replace(id, newtrade);
    }

    public void deleteTrade(String id) {
        trademap.remove(id);
    }

}
