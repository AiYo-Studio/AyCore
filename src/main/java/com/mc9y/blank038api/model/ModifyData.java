package com.mc9y.blank038api.model;

import com.mc9y.blank038api.interfaces.CustomExecute;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 文件修改记录类
 *
 * @author Blank038
 */
public class ModifyData {
    private final HashMap<File, Long> modifyMap = new HashMap<>();
    private final CustomExecute execute;
    private final boolean async;
    private final PluginData pluginData;
    private long lastModify = System.currentTimeMillis();

    public ModifyData(PluginData pluginData, File[] files, CustomExecute execute, boolean async) {
        for (File i : files) {
            modifyMap.put(i, i.lastModified());
        }
        this.pluginData = pluginData;
        this.execute = execute;
        this.async = async;
        // 存放
        pluginData.addModifyData(this);
    }

    public synchronized void check() {
        boolean run = false;
        for (Map.Entry<File, Long> entry : new HashSet<>(modifyMap.entrySet())) {
            if (entry.getKey().exists() && entry.getKey().lastModified() > entry.getValue()
                    && entry.getKey().lastModified() > lastModify) {
                modifyMap.replace(entry.getKey(), entry.getKey().lastModified());
                lastModify = entry.getValue();
                run = true;
                break;
            }
        }
        if (run) {
            if (async) {
                execute.run();
            } else {
                Bukkit.getScheduler().runTask(pluginData.getPlugin(), execute::run);
            }
        }
    }

    /**
     * 获取插件源数据
     *
     * @return 插件源数据
     */
    public PluginData getPluginData() {
        return pluginData;
    }

    /**
     * 获取文件修改后的执行器
     *
     * @return 执行器
     */
    public CustomExecute getExecute() {
        return execute;
    }
}