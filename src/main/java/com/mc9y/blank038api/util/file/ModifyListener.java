package com.mc9y.blank038api.util.file;

import com.mc9y.blank038api.Blank038API;
import com.mc9y.blank038api.interfaces.CustomExecute;
import com.mc9y.blank038api.model.ModifyData;
import com.mc9y.blank038api.model.PluginData;
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
    public static void addListener(Plugin plugin, CustomExecute execute, boolean async, File... files) {
        new ModifyData(Blank038API.getBlank038API().dataMap.getOrDefault(plugin,
                Blank038API.getBlank038API().dataMap.put(plugin, new PluginData(plugin))), files, execute, async);
    }

    /**
     * 移除文件修改监听
     *
     * @param plugin     目标插件
     * @param modifyData 文件修改数据类
     */
    public static void removeListener(Plugin plugin, ModifyData[] modifyData) {
        if (Blank038API.getBlank038API().dataMap.containsKey(plugin)) {
            for (ModifyData md : modifyData) {
                Blank038API.getBlank038API().dataMap.get(plugin).removeModifyData(md);
            }
        }
    }

    /**
     * 移除文件修改监听
     *
     * @param plugin 目标插件
     */
    public static void removeListener(Plugin plugin) {
        if (Blank038API.getBlank038API().dataMap.containsKey(plugin)) {
            Blank038API.getBlank038API().dataMap.get(plugin).removeAllModifyData();
        }
    }

    /**
     * 注册插件数据
     *
     * @param plugin 目标插件
     * @return 插件数据
     */
    public static PluginData registerPluginData(Plugin plugin) {
        if (!Blank038API.getBlank038API().dataMap.containsKey(plugin)) {
            return Blank038API.getBlank038API().dataMap.put(plugin, new PluginData(plugin));
        }
        return Blank038API.getBlank038API().dataMap.get(plugin);
    }
}
