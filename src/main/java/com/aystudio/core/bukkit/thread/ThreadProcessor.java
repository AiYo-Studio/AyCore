package com.aystudio.core.bukkit.thread;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Blank038
 */
public class ThreadProcessor {
    public static Integer taskId = 0;
    public static final Map<Integer, BlankThread> THREAD_MAP = new HashMap<>();
    public static final Map<String, List<Integer>> PLUGIN_TASK_IDS = new HashMap<>();

    public static void crateTask(Plugin plugin, BlankThread thread) {
        Integer id = ++taskId;
        THREAD_MAP.put(id, thread);
        if (!PLUGIN_TASK_IDS.containsKey(plugin.getName())) {
            PLUGIN_TASK_IDS.put(plugin.getName(), new ArrayList<>());
        }
        PLUGIN_TASK_IDS.get(plugin.getName()).add(id);
        new BlankTask(thread, id).start();
    }

    public static void stopAll() {
        THREAD_MAP.forEach((key, value) -> value.cancel());
        THREAD_MAP.clear();
    }

    public static void stopPluginTask(String pluginName) {
        if (PLUGIN_TASK_IDS.containsKey(pluginName)) {
            for (Integer taskId : PLUGIN_TASK_IDS.get(pluginName)) {
                if (THREAD_MAP.containsKey(taskId)) {
                    THREAD_MAP.get(taskId).cancel();
                    THREAD_MAP.remove(taskId);
                }
            }
            PLUGIN_TASK_IDS.remove(pluginName);
        }
    }
}
