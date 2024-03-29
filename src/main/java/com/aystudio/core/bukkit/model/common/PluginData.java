package com.aystudio.core.bukkit.model.common;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.helper.SchedulerHelper;
import com.aystudio.core.bukkit.model.configuration.ConfigurationData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Blank038
 */
public class PluginData {
    private final List<ModifyData> modifyDataList = new ArrayList<>();
    private final Plugin plugin;
    private final Object bukkitTask;
    private final ConfigurationData configurationData;

    public PluginData(Plugin plugin) {
        this.plugin = plugin;
        configurationData = new ConfigurationData();
        // 开始线程监听
        bukkitTask = SchedulerHelper.runTaskTimerAsync(plugin, () -> {
            for (ModifyData modifyData : new ArrayList<>(modifyDataList)) {
                modifyData.check();
            }
        }, 20L, 20L);
    }

    public void addModifyData(ModifyData modifyData) {
        if (!modifyDataList.contains(modifyData)) {
            modifyDataList.add(modifyData);
        }
    }

    public void addConfiguration(String key, FileConfiguration data) {
        configurationData.addConfiguration(key, data);
    }

    public void removeModifyData(ModifyData modifyData) {
        modifyDataList.remove(modifyData);
    }

    public void removeAllModifyData() {
        modifyDataList.clear();
    }

    public void disable() {
        SchedulerHelper.cancelTask(bukkitTask);
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public FileConfiguration getConfiguration(String key) {
        return configurationData.getConfig(key);
    }

    public static PluginData getData(Plugin plugin) {
        return AyCore.getInstance().dataMap.getOrDefault(plugin, null);
    }
}
