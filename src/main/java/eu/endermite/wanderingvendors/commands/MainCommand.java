package eu.endermite.wanderingvendors.commands;

import eu.endermite.wanderingvendors.WanderingVendors;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("wanderingvendors.command")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("give")) {
                if (!sender.hasPermission("wanderingvendors.command.reload")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                    return true;
                }
                GiveItem.giveItem(sender, args);
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("wanderingvendors.command.reload")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                    return true;
                }
                WanderingVendors.getPlugin().reloadConfigData();
                sender.sendMessage(ChatColor.YELLOW + "Config reloaded!");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "No such subcommand");
            }
        }


        return true;
    }
}
