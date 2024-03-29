package com.aystudio.core.bukkit.model.common;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.interfaces.CustomExecute;
import lombok.Getter;

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
    /**
     * -- GETTER --
     * 获取文件修改后的执行器
     */
    @Getter
    private final CustomExecute<ModifyData> execute;
    private final boolean async;
    /**
     * -- GETTER --
     * 获取插件源数据
     */
    @Getter
    private final PluginData pluginData;
    private long lastModify = System.currentTimeMillis();

    public ModifyData(PluginData pluginData, File[] files, CustomExecute<ModifyData> execute, boolean async) {
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
                execute.run(this);
            } else {
                AyCore.getPlatformApi().runTask(pluginData.getPlugin(), () -> execute.run(this));
            }
        }
    }

}