package eu.endermite.wanderingvendors.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("wanderingvendors.command")) {
            sender.sendMessage(ChatColor.RED+"You don't have permission to do this!");
            return true;
        }


        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("give")) {
                    GiveItem.giveItem((Player) sender, args);
                } else {
                    sender.sendMessage(ChatColor.RED+"No such subcommand");
                }
            }
        }

        return true;
    }
}
