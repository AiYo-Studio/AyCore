package com.aystudio.core.bukkit.helper;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Blank038
 */
public class SchedulerHelper {

    public static void runTask(Plugin plugin, Runnable runnable) {
        if (PlatformHelper.isFolia()) {
            Bukkit.getServer().getGlobalRegionScheduler().execute(plugin, runnable);
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static void runTaskAsync(Plugin plugin, Runnable runnable) {
        if (PlatformHelper.isFolia()) {
            Consumer<io.papermc.paper.threadedregions.scheduler.ScheduledTask> consumer = (task) -> runnable.run();
            Bukkit.getServer().getAsyncScheduler().runNow(plugin, consumer);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
    }

    public static void runTaskAsync(Plugin plugin, Runnable runnable, long delay) {
        if (PlatformHelper.isFolia()) {
            Consumer<io.papermc.paper.threadedregions.scheduler.ScheduledTask> consumer = (task) -> runnable.run();
            Bukkit.getServer().getAsyncScheduler().runDelayed(plugin, consumer, Math.max(1, delay / 20), TimeUnit.SECONDS);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
        }
    }

    public static Object runTaskTimerAsync(Plugin plugin, Runnable runnable, long delay, long period) {
        if (PlatformHelper.isFolia()) {
            Consumer<io.papermc.paper.threadedregions.scheduler.ScheduledTask> consumer = (task) -> runnable.run();
            return Bukkit.getServer().getAsyncScheduler().runAtFixedRate(plugin, consumer, Math.max(1, delay / 20), Math.max(1, period / 20), TimeUnit.SECONDS);
        } else {
            return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
        }
    }

    public static void cancelTask(Object o) {
        if (o == null) {
            return;
        }
        if (PlatformHelper.isFolia()) {
            ((io.papermc.paper.threadedregions.scheduler.ScheduledTask) o).cancel();
        } else {
            ((BukkitTask) o).cancel();
        }
    }
}
