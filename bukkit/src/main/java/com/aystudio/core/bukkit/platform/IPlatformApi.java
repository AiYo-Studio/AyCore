package com.aystudio.core.bukkit.platform;

import com.aystudio.core.bukkit.platform.wrapper.ITaskWrapper;
import org.bukkit.plugin.java.JavaPlugin;

public interface IPlatformApi {

    ITaskWrapper runTask(JavaPlugin plugin, Runnable runnable);

    ITaskWrapper runTaskLater(JavaPlugin plugin, Runnable runnable, long delay);

    ITaskWrapper runTaskTimer(JavaPlugin plugin, Runnable runnable, long delay, long period);
    
    ITaskWrapper runTaskAsynchronously(JavaPlugin plugin, Runnable runnable);

    ITaskWrapper runTaskLaterAsynchronously(JavaPlugin plugin, Runnable runnable, long delay);

    ITaskWrapper runTaskTimerAsynchronously(JavaPlugin plugin, Runnable runnable, long delay, long period);
}
