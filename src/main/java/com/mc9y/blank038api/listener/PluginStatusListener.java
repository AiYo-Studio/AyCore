package com.mc9y.blank038api.listener;

import com.mc9y.blank038api.Blank038API;
import com.mc9y.blank038api.model.PluginData;
import com.mc9y.blank038api.thread.ThreadProcessor;
import com.mc9y.blank038api.util.file.ModifyListener;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

/**
 * @author Blank038
 */
public class PluginStatusListener implements Listener {

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        String pluginName = event.getPlugin().getName();
        ThreadProcessor.stopPluginTask(pluginName);
        Blank038API.getCommandRegistry().unregisterCommand(event.getPlugin());
        ModifyListener.removeListener(event.getPlugin());
        PluginData pluginData = Blank038API.getBlank038API().dataMap.remove(event.getPlugin());
        if (pluginData != null) {
            pluginData.disable();
        }
    }

    @EventHandler
    public void onCommandPerform(PlayerCommandPreprocessEvent event) {
        if (sendCommand(event.getPlayer(), event.getMessage())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onServerCommandEvent(ServerCommandEvent event) {
        if (sendCommand(event.getSender(), event.getCommand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onConsolePerform(RemoteServerCommandEvent event) {
        if (sendCommand(event.getSender(), event.getCommand())) {
            event.setCancelled(true);
        }
    }

    private boolean sendCommand(CommandSender sender, String message) {
        String lastCommand = message.replaceAll("\\s+", " "), command = lastCommand;
        String[] args = new String[0];
        if (lastCommand.contains(" ")) {
            String[] split = StringUtils.split(lastCommand, " ");
            args = new String[split.length - 1];
            command = split[0].replace("/", "");
            if (args.length - 1 >= 0) {
                System.arraycopy(split, 1, args, 1, args.length - 1);
            }
        }
        return Blank038API.getCommandRegistry().performCommand(sender, command.replace("/", ""), args);
    }
}
