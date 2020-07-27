package eu.endermite.wanderingvendors.gui;

import eu.endermite.wanderingvendors.WanderingVendors;
import eu.endermite.wanderingvendors.config.CreatorTradesConfig;
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
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradeCreator implements Listener {

    private static int tradeuses = 1;

    private static final NamespacedKey key = new NamespacedKey(WanderingVendors.getPlugin(), "guiitem");
    private static final NamespacedKey tradeidkey = new NamespacedKey(WanderingVendors.getPlugin(), "tradeid");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = ((Player) e.getWhoClicked()).getPlayer();
            if (!e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("Trade Creation"))
                return;

            if (e.getClickedInventory() == null)
                return;

            if (e.getCurrentItem() == null)
                return;

            if (!e.getCurrentItem().hasItemMeta())
                return;

            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING) == null) {
                return;
            }

            String id;

            switch (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)) {
                case "block":
                    e.setCancelled(true);
                    break;
                case "tradeuses":
                    if (e.getAction().equals(InventoryAction.PICKUP_ALL) && tradeuses < 64) {
                        tradeuses++;
                        e.getClickedInventory().setItem(e.getSlot(), getUsesDisplay(tradeuses));
                    } else if (e.getAction().equals(InventoryAction.PICKUP_HALF) && tradeuses > 1) {
                        tradeuses = tradeuses-1;
                        e.getClickedInventory().setItem(e.getSlot(), getUsesDisplay(tradeuses));
                    }
                    e.setCancelled(true);
                    break;
                case "delete":
                    id = e.getClickedInventory().getItem(1).getItemMeta().getPersistentDataContainer().get(tradeidkey, PersistentDataType.STRING);
                    if (id != null) {
                        try {
                            WanderingVendors.getConfigCache().deleteTrade(id);
                        } catch (NullPointerException ignored) {}
                        try {
                            CreatorTradesConfig.deleteTrade(id);
                        } catch (NullPointerException ignored) {}
                    }
                    e.setCancelled(true);
                    p.closeInventory();
                    break;
                case "save":
                    ItemStack ingridient1 = e.getClickedInventory().getItem(3);
                    ItemStack ingridient2 = e.getClickedInventory().getItem(4);
                    ItemStack result = e.getClickedInventory().getItem(5);

                    if (ingridient1 == null && ingridient2 == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTrade needs to have at least 1 ingridient!"));
                        e.setCancelled(true);
                        break;
                    }
                    if (result == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cResult cannot be empty!"));
                        e.setCancelled(true);
                        break;
                    } else {
                        MerchantRecipe recipe = new MerchantRecipe(result, tradeuses);
                        if (ingridient1 != null) {
                            recipe.addIngredient(ingridient1);
                        }
                        if (ingridient2 != null) {
                            recipe.addIngredient(ingridient2);
                        }
                        try {
                            id = e.getClickedInventory().getItem(1).getItemMeta().getPersistentDataContainer().get(tradeidkey, PersistentDataType.STRING);
                            if (id == null) {
                                UUID uuid = UUID.randomUUID();
                                WanderingVendors.getConfigCache().addTrade(uuid.toString(), recipe);
                                CreatorTradesConfig.saveTrade(uuid.toString(), recipe);
                            } else {
                                WanderingVendors.getConfigCache().editTrade(id, recipe);
                                CreatorTradesConfig.saveTrade(id, recipe);
                            }
                        } catch (NullPointerException | IllegalArgumentException e1) {
                            e1.printStackTrace();
                        }
                        RefreshGuis.refresh();
                        p.closeInventory();
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

    public void openGui(Player player) {
        tradeuses = 1;
        Inventory inv = Bukkit.createInventory(null, InventoryType.DROPPER, "Trade Creation");

        ItemStack block = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemStack blockIcon = IconCreator.createIcon(block, "block", " ");

        ItemStack instruction = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
        ItemStack instructionIcon = IconCreator.createIcon(instruction, "block", " ");

        ItemStack save = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemStack saveIcon = IconCreator.createIcon(save, "save", "&l&eSave Trade");

        ItemStack cancel = new ItemStack(Material.BARRIER, 1);
        ItemStack cancelIcon = IconCreator.createIcon(cancel, "cancel", "&l&cCancel");

        inv.setItem(0, getUsesDisplay(tradeuses));
        inv.setItem(1, instructionIcon);
        inv.setItem(2, blockIcon);
        inv.setItem(6, cancelIcon);
        inv.setItem(7, blockIcon);
        inv.setItem(8, saveIcon);

        player.openInventory(inv);
    }

    public void openGui(Player player, String recipeID) {
        MerchantRecipe recipe = WanderingVendors.getConfigCache().getMerchantTrades().get(recipeID);
        tradeuses = recipe.getMaxUses();
        Inventory inv = Bukkit.createInventory(null, InventoryType.DROPPER, "Trade Creation");

        ItemStack block = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemStack blockIcon = IconCreator.createIcon(block, "block", " ");

        ItemStack instruction = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
        ItemStack instructionIcon = IconCreator.createIcon(instruction, "block", " ");

        ItemStack delete = new ItemStack(Material.LAVA_BUCKET, 1);
        ItemStack deleteIcon = IconCreator.createIcon(delete, "delete", "&4&lDelete Trade");

        ItemStack save = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemStack saveIcon = IconCreator.createIcon(save, "save", "&l&eSave Trade");

        ItemStack cancel = new ItemStack(Material.BARRIER, 1);
        ItemStack cancelIcon = IconCreator.createIcon(cancel, "cancel", "&l&cCancel");

        inv.setItem(0, getUsesDisplay(tradeuses));
        inv.setItem(1, instructionIcon);
        inv.setItem(2, deleteIcon);
        inv.setItem(3, recipe.getIngredients().get(0));
        if (recipe.getIngredients().size() > 1) {
            inv.setItem(4, recipe.getIngredients().get(1));
        }
        inv.setItem(5, recipe.getResult());
        inv.setItem(6, cancelIcon);
        inv.setItem(7, blockIcon);
        inv.setItem(8, saveIcon);

        player.openInventory(inv);
    }

    public static ItemStack getUsesDisplay(int amount) {
        ItemStack tradeamount = new ItemStack(Material.STONE_BUTTON, amount);
        ItemMeta tradeamountmeta = tradeamount.getItemMeta();
        tradeamountmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&lTrade uses"));
        tradeamountmeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "tradeuses");
        List<String> tamountlore = new ArrayList<>();
        tamountlore.add("Uses: "+amount);
        tamountlore.add("LMB - add 1");
        tamountlore.add("RMB - remove 1");
        tradeamountmeta.setLore(tamountlore);
        tradeamount.setItemMeta(tradeamountmeta);
        return tradeamount;
    }
}
