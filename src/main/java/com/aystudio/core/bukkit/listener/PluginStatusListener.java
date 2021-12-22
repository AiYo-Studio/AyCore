package com.aystudio.core.bukkit.listener;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.model.common.PluginData;
import com.aystudio.core.bukkit.thread.ThreadProcessor;
import com.aystudio.core.bukkit.util.file.ModifyListener;
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
        AyCore.getCommandRegistry().unregisterCommand(event.getPlugin());
        ModifyListener.removeListener(event.getPlugin());
        PluginData pluginData = AyCore.getBlank038API().dataMap.remove(event.getPlugin());
        if (pluginData != null) {
            pluginData.disable();
        }
    }
}
