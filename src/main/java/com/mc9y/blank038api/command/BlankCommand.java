package com.mc9y.blank038api.command;

import com.mc9y.blank038api.Blank038API;
import com.mc9y.blank038api.command.registry.CustomCommand;
import com.mc9y.blank038api.util.common.EntityUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Blank038
 */
public class BlankCommand {

//    @CustomCommand()
//    public void perform(CommandSender commandSender) {
//        if (commandSender.hasPermission("bapi.admin")) {
//            Blank038API.getBlank038API().loadConfig();
//            commandSender.sendMessage(ChatColor.GREEN + "Success reload configuration.");
//        }
//    }

    @CustomCommand()
    public void performPlayer(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location location = player.getLocation().clone();
            location.getBlock().setType(Material.STONE);
            EntityUtil.createFallingBlock(location.getBlock(), location.clone().add(5, 0, 5), 1);
        }
    }
}