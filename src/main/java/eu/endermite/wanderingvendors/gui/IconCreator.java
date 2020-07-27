package eu.endermite.wanderingvendors.gui;

import eu.endermite.wanderingvendors.WanderingVendors;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class IconCreator {

    private static final NamespacedKey key = new NamespacedKey(WanderingVendors.getPlugin(), "guiitem");

    public static ItemStack createIcon(ItemStack itemstack, String type, String displayname) {

        ItemMeta meta = itemstack.getItemMeta().clone();
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, type);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayname));
        itemstack.setItemMeta(meta);
        return itemstack;

    }

}
