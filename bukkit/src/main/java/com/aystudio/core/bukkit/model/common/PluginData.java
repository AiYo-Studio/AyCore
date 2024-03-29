package com.aystudio.core.bukkit.model.common;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.model.configuration.ConfigurationData;
import com.aystudio.core.bukkit.platform.wrapper.ITaskWrapper;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Blank038
 */
public class PluginData {
    private final List<ModifyData> modifyDataList = new ArrayList<>();
    @Getter
    private final JavaPlugin plugin;
    private final ITaskWrapper bukkitTask;
    private final ConfigurationData configurationData;

    public PluginData(JavaPlugin plugin) {
        this.plugin = plugin;
        configurationData = new ConfigurationData();
        // 开始线程监听
        bukkitTask = AyCore.getPlatformApi().runTaskTimerAsynchronously(plugin, () -> {
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

    public FileConfiguration getConfiguration(String key) {
        return configurationData.getConfig(key);
    }

    public static PluginData getData(JavaPlugin plugin) {
        return AyCore.getInstance().dataMap.getOrDefault(plugin, null);
    }
}
