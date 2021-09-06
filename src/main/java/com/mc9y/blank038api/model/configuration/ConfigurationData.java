package com.mc9y.blank038api.model.configuration;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

/**
 * 多源配置类, 可用于多语言配置
 *
 * @author Blank038
 * @since  2020/11/08
 */
public class ConfigurationData {
    private final HashMap<String, FileConfiguration> MAP = new HashMap<>();

    public void addConfiguration(String key, FileConfiguration section) {
        MAP.remove(key);
        MAP.put(key, section);
    }

    public void removeConfiguration(String key) {
        MAP.remove(key);
    }

    public FileConfiguration getConfig(String key) {
        return MAP.getOrDefault(key, null);
    }
}