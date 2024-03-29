package com.aystudio.core.bukkit.platform.folia;

import com.aystudio.core.bukkit.platform.IPlatformApi;
import com.aystudio.core.bukkit.platform.folia.wrapper.FoliaTaskWrapper;
import com.aystudio.core.bukkit.platform.wrapper.ITaskWrapper;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

/**
 * @author Blank038
 */
public class FoliaPlatformApi implements IPlatformApi {

    @Override
    public ITaskWrapper runTask(JavaPlugin plugin, Runnable runnable) {
        ScheduledTask task = Bukkit.getServer().getGlobalRegionScheduler().run(plugin, (t) -> runnable.run());
        return new FoliaTaskWrapper(task);
    }

    @Override
    public ITaskWrapper runTaskLater(JavaPlugin plugin, Runnable runnable, long delay) {
        ScheduledTask task = Bukkit.getServer().getGlobalRegionScheduler().runDelayed(plugin, (t) -> runnable.run(), delay);
        return new FoliaTaskWrapper(task);
    }

    @Override
    public ITaskWrapper runTaskAsynchronously(JavaPlugin plugin, Runnable runnable) {
        ScheduledTask task = Bukkit.getServer().getAsyncScheduler().runNow(plugin, (t) -> runnable.run());
        return new FoliaTaskWrapper(task);
    }

    @Override
    public ITaskWrapper runTaskLaterAsynchronously(JavaPlugin plugin, Runnable runnable, long delay) {
        ScheduledTask task = Bukkit.getServer().getAsyncScheduler().runDelayed(plugin, (t) -> runnable.run(), delay / 20, TimeUnit.SECONDS);
        return new FoliaTaskWrapper(task);
    }

    @Override
    public ITaskWrapper runTaskTimerAsynchronously(JavaPlugin plugin, Runnable runnable, long delay, long period) {
        ScheduledTask task = Bukkit.getServer().getAsyncScheduler().runAtFixedRate(plugin, (t) -> runnable.run(), delay / 20, period / 20, TimeUnit.SECONDS);
        return new FoliaTaskWrapper(task);
    }
}
