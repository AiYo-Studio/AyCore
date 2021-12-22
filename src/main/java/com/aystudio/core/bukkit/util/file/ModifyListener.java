package com.aystudio.core.bukkit.util.file;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.interfaces.CustomExecute;
import com.aystudio.core.bukkit.model.common.ModifyData;
import com.aystudio.core.bukkit.model.common.PluginData;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * 文件修改监听
 *
 * @author Blank038
 */
@SuppressWarnings("unused")
public class ModifyListener {

    /**
     * 增加文件修改监听
     *
     * @param plugin  源插件
     * @param execute 修改执行器
     * @param async   是否异步
     * @param files   监听文件
     */
    public static void addListener(Plugin plugin, CustomExecute<ModifyData> execute, boolean async, File... files) {
        new ModifyData(AyCore.getBlank038API().dataMap.getOrDefault(plugin,
                AyCore.getBlank038API().dataMap.put(plugin, new PluginData(plugin))), files, execute, async);
    }

    /**
     * 移除文件修改监听
     *
     * @param plugin     目标插件
     * @param modifyData 文件修改数据类
     */
    public static void removeListener(Plugin plugin, ModifyData[] modifyData) {
        if (AyCore.getBlank038API().dataMap.containsKey(plugin)) {
            for (ModifyData md : modifyData) {
                AyCore.getBlank038API().dataMap.get(plugin).removeModifyData(md);
            }
        }
    }

    /**
     * 移除文件修改监听
     *
     * @param plugin 目标插件
     */
    public static void removeListener(Plugin plugin) {
        if (AyCore.getBlank038API().dataMap.containsKey(plugin)) {
            AyCore.getBlank038API().dataMap.get(plugin).removeAllModifyData();
        }
    }

    /**
     * 注册插件数据
     *
     * @param plugin 目标插件
     * @return 插件数据
     */
    public static PluginData registerPluginData(Plugin plugin) {
        if (!AyCore.getBlank038API().dataMap.containsKey(plugin)) {
            return AyCore.getBlank038API().dataMap.put(plugin, new PluginData(plugin));
        }
        return AyCore.getBlank038API().dataMap.get(plugin);
    }
}
