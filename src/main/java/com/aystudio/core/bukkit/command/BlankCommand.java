package com.aystudio.core.bukkit.command;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.command.registry.CustomCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Blank038
 */
public class BlankCommand {

    @CustomCommand()
    public void perform(CommandSender commandSender) {
        if (commandSender.hasPermission("bapi.admin")) {
            AyCore.getInstance().loadConfig();
            commandSender.sendMessage(ChatColor.GREEN + "Success reload configuration.");
        }
    }
}