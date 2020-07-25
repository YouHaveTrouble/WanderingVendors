package eu.endermite.wanderingvendors.commands;

import eu.endermite.wanderingvendors.WanderingVendors;
import eu.endermite.wanderingvendors.gui.TradeCreator;
import eu.endermite.wanderingvendors.gui.TradeList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class MainCommand implements TabExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("wanderingvendors.command")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("wanderingvendors.command.reload")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                    return true;
                }
                WanderingVendors.getPlugin().reloadConfigData();
                sender.sendMessage(ChatColor.YELLOW + "Config reloaded!");
                return true;
            } else if (args[0].equalsIgnoreCase("createtrade")) {
            if (!sender.hasPermission("wanderingvendors.command.createtrade")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                return true;
            }
            if (sender instanceof Player) {
                TradeCreator creator = new TradeCreator();
                creator.openGui((Player) sender);
            } else {
                sender.sendMessage(ChatColor.RED + "Can only be used in-game");
            }
            return true;
            } else if (args[0].equalsIgnoreCase("trades")) {
                if (!sender.hasPermission("wanderingvendors.command.trades")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                    return true;
                }
                if (sender instanceof Player) {
                    TradeList list = new TradeList();
                    list.openGui((Player) sender, 0);
                } else {
                    sender.sendMessage(ChatColor.RED + "Can only be used in-game");
                }
                return true;
            }
            else {
                sender.sendMessage(ChatColor.RED + "No such subcommand");
            }
        } else {
            sender.sendMessage(ChatColor.YELLOW + "WanderingVendors by YouHaveTrouble");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> all =  new ArrayList<>();
        all.add("reload");
        all.add("createtrade");
        all.add("trades");

        if (args.length == 1) {
            List<String> result = new ArrayList<>();
            for (String cmd : all) {
                if (cmd.startsWith(args[0])) {
                    result.add(cmd);
                }
            }
            return result;
        }

        return null;
    }
}
