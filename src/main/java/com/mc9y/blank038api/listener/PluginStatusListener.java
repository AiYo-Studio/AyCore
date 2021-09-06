package com.mc9y.blank038api.listener;

import com.mc9y.blank038api.Blank038API;
import com.mc9y.blank038api.model.common.PluginData;
import com.mc9y.blank038api.thread.ThreadProcessor;
import com.mc9y.blank038api.util.file.ModifyListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

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
}
