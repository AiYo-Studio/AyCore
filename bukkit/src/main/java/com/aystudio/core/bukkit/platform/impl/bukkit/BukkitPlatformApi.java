package com.aystudio.core.bukkit.platform.impl.bukkit;

import com.aystudio.core.bukkit.platform.IPlatformApi;
import com.aystudio.core.bukkit.platform.impl.bukkit.warpper.BukkitTaskWrapper;
import com.aystudio.core.bukkit.platform.wrapper.ITaskWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Blank038
 */
public class BukkitPlatformApi implements IPlatformApi {

    @Override
    public ITaskWrapper runTask(JavaPlugin plugin, Runnable runnable) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTask(plugin, runnable);
        return new BukkitTaskWrapper(bukkitTask);
    }

    @Override
    public ITaskWrapper runTaskLater(JavaPlugin plugin, Runnable runnable, long delay) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        return new BukkitTaskWrapper(bukkitTask);
    }

    @Override
    public ITaskWrapper runTaskTimer(JavaPlugin plugin, Runnable runnable, long delay, long period) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
        return new BukkitTaskWrapper(bukkitTask);
    }

    @Override
    public ITaskWrapper runTaskAsynchronously(JavaPlugin plugin, Runnable runnable) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        return new BukkitTaskWrapper(bukkitTask);
    }

    @Override
    public ITaskWrapper runTaskLaterAsynchronously(JavaPlugin plugin, Runnable runnable, long delay) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
        return new BukkitTaskWrapper(bukkitTask);
    }

    @Override
    public ITaskWrapper runTaskTimerAsynchronously(JavaPlugin plugin, Runnable runnable, long delay, long period) {
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
        return new BukkitTaskWrapper(bukkitTask);
    }
}
