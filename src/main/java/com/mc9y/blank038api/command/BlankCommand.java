package com.mc9y.blank038api.command;

import com.mc9y.blank038api.Blank038API;
import com.mc9y.blank038api.command.registry.CustomCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Blank038
 */
public class BlankCommand {

    @CustomCommand()
    public boolean perform(CommandSender commandSender) {
        if (commandSender.hasPermission("bapi.admin")) {
            Blank038API.getBlank038API().loadConfig();
            commandSender.sendMessage(ChatColor.GREEN + "Success reload configuration.");
        }
        return false;
    }
}