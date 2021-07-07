package com.mc9y.blank038api.model;

import com.mc9y.blank038api.Blank038API;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Blank038
 */
public class PluginData {
    private final List<ModifyData> modifyDataList = new ArrayList<>();
    private final Plugin plugin;
    private final BukkitTask bukkitTask;
    private final ConfigurationData configurationData;

    public PluginData(Plugin plugin) {
        this.plugin = plugin;
        configurationData = new ConfigurationData();
        // 开始线程监听
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
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
        bukkitTask.cancel();
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public FileConfiguration getConfiguration(String key) {
        return configurationData.getConfig(key);
    }

    public static PluginData getData(Plugin plugin) {
        return Blank038API.getBlank038API().dataMap.getOrDefault(plugin, null);
    }
}
