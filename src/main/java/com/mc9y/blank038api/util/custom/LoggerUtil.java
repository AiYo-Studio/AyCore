package com.mc9y.blank038api.util.custom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * @author Blank038
 */
public class LoggerUtil {
    private static final HashMap<Class<? extends JavaPlugin>, LoggerUtil> LOGGER_MAP = new HashMap<>();
    private String PREFIX;

    private LoggerUtil() {
        this("* ");
    }

    private LoggerUtil(String prefix) {
        this.PREFIX = prefix;
    }

    public void setPrefix(String prefix) {
        this.PREFIX = prefix;
    }

    public void log(String text) {
        this.log(true, text);
    }

    public void log(boolean hasPrefix, String text) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', (hasPrefix ? this.PREFIX : "") + text));
    }

    public static void register(Class<? extends JavaPlugin> c, String prefix) {
        if (LOGGER_MAP.containsKey(c)) {
            return;
        }
        LOGGER_MAP.put(c, new LoggerUtil(prefix));
    }

    public static LoggerUtil getOrRegister(Class<? extends JavaPlugin> c, String... prefix) {
        if (LOGGER_MAP.containsKey(c)) {
            return LOGGER_MAP.get(c);
        }
        LOGGER_MAP.put(c, prefix.length > 0 ? new LoggerUtil(prefix[0]) : new LoggerUtil());
        return LOGGER_MAP.get(c);
    }
}