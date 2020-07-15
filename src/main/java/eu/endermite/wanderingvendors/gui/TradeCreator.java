package eu.endermite.wanderingvendors.gui;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class TradeCreator implements Listener {

    private static final NamespacedKey key = new NamespacedKey(WanderingVendors.getPlugin(), "guiitem");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = ((Player) e.getWhoClicked()).getPlayer();
            if (!e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("Trade Creation")) {
                return;
            }
            if (e.getCurrentItem() == null) {
                return;
            }

            if (!e.getCurrentItem().hasItemMeta())
                return;
            switch (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)) {
                case "block":
                    e.setCancelled(true);
                    break;
                case "save":
                    if (e.getClickedInventory() != null) {
                        ItemStack ing1 = e.getClickedInventory().getItem(3);
                        ItemStack ing2 = e.getClickedInventory().getItem(4);
                        ItemStack res = e.getClickedInventory().getItem(5);

                        if (ing1 == null) {
                            ing1 = new ItemStack(Material.AIR, 1);
                        }
                        if (ing2 == null) {
                            ing2 = new ItemStack(Material.AIR, 1);
                        }
                        if (res == null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cResult cannot be empty!"));

                        } else {
                            p.closeInventory();
                        }

                        System.out.println(ing1+" "+ing2+" "+res);
                    }
                    e.setCancelled(true);
                    break;
                case "cancel":
                    e.setCancelled(true);
                    p.closeInventory();
                    break;
                default:
                    break;
            }
        }
    }

    public static void openCreator(Player player) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.DROPPER, "Trade Creation");

        ItemStack block = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta blockmeta = block.getItemMeta();
        blockmeta.setDisplayName(" ");
        blockmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block");
        block.setItemMeta(blockmeta);

        ItemStack instructioning = new ItemStack(Material.PAPER, 1);
        ItemMeta instructionmeta = instructioning.getItemMeta();
        instructionmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&lInstructions"));
        instructionmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block");
        instructioning.setItemMeta(instructionmeta);

        ItemStack save = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta savemeta = save.getItemMeta();
        savemeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lSave Trade"));
        savemeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "save");
        save.setItemMeta(savemeta);

        ItemStack cancel = new ItemStack(Material.BARRIER, 1);
        ItemMeta cancelmeta = cancel.getItemMeta();
        cancelmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lCancel"));
        cancelmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "cancel");
        cancel.setItemMeta(cancelmeta);


        inv.setItem(0, block);
        inv.setItem(1, instructioning);
        inv.setItem(2, block);
        inv.setItem(6, cancel);
        inv.setItem(7, block);
        inv.setItem(8, save);

        player.openInventory(inv);

    }

}
