package com.mc9y.blank038api.plugin;

import com.mc9y.blank038api.interfaces.CustomExecute;
import com.mc9y.blank038api.util.common.CommonUtil;
import com.mc9y.blank038api.util.custom.LoggerUtil;
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
