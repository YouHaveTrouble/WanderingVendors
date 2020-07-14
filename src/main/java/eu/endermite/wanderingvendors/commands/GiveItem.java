package eu.endermite.wanderingvendors.commands;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveItem {

    public static void giveItem(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;

            if (args.length >= 3) {
                try {
                    if (args[2].equalsIgnoreCase("result") || args[2].equalsIgnoreCase("0")) {
                        sender.getInventory().addItem(WanderingVendors.getConfigCache().getItems().get(args[1]).get(0));
                        sender.sendMessage("You recieved result of trade " + args[1]);
                    } else if (args[2].equalsIgnoreCase("ingridient1") || args[2].equalsIgnoreCase("1")) {
                        sender.getInventory().addItem(WanderingVendors.getConfigCache().getItems().get(args[1]).get(1));
                        sender.sendMessage("You recieved ingridient #1 of trade " + args[1]);
                    } else if (args[2].equalsIgnoreCase("ingridient2") || args[2].equalsIgnoreCase("2")) {
                        sender.getInventory().addItem(WanderingVendors.getConfigCache().getItems().get(args[1]).get(2));
                        sender.sendMessage("You recieved ingridient #2 of trade " + args[1]);
                    }

                } catch (NullPointerException e) {
                    sender.sendMessage(ChatColor.RED + "trade\"" + args[1] + "\" doesn't exist.");
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "Usage: /wanderingvendors give <trade_name> <result/ingridient1/ingridient2>");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "Only possible in-game.");
        }
    }

}
