package com.aystudio.core.bukkit.plugin;

import com.aystudio.core.bukkit.interfaces.CustomExecute;
import com.aystudio.core.bukkit.util.common.CommonUtil;
import com.aystudio.core.bukkit.util.custom.LoggerUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Blank038
 */
public abstract class AyPlugin extends JavaPlugin {
    private final LoggerUtil CONSOLE_LOGGER;

    public AyPlugin() {
        this.CONSOLE_LOGGER = LoggerUtil.getOrRegister(this.getClass(), this.getDescription().getName());
    }

    public LoggerUtil getConsoleLogger() {
        return this.CONSOLE_LOGGER;
    }

    public void saveResource(String source, String target) {
        this.saveResource(source, target, false, null);
    }

    public void saveResource(String source, String target, boolean replace, CustomExecute<File> execute) {
        CommonUtil.saveResource(this, source, target, replace, execute);
    }
}
