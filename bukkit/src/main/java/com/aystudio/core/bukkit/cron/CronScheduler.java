package com.aystudio.core.bukkit.cron;

import com.aystudio.core.bukkit.cron.context.ContextWrapper;
import com.aystudio.core.common.util.Pair;
import org.bukkit.plugin.java.JavaPlugin;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;

public class CronScheduler {
    private final JavaPlugin plugin;
    private Scheduler scheduler;

    private CronScheduler(JavaPlugin plugin, Properties properties) {
        this.plugin = plugin;
        StdSchedulerFactory factory = new StdSchedulerFactory();
        try {
            factory.initialize(properties);
            this.scheduler = factory.getScheduler();
            this.scheduler.start();
        } catch (SchedulerException e) {
            this.plugin.getLogger().log(Level.WARNING, e, () -> "Failed to initialize StdSchedulerFactory.");
        }
    }

    public CronJob createJob(String identity, String group, String expression, Consumer<ContextWrapper> contextWrapperConsumer) {
        try {
            return new CronJob(this.scheduler, identity, group, expression, contextWrapperConsumer);
        } catch (SchedulerException e) {
            this.plugin.getLogger().log(Level.WARNING, e, () -> "Failed to create job.");
        }
        return null;
    }

    public void stopJobs() {
        try {
            scheduler.clear();
        } catch (SchedulerException e) {
            this.plugin.getLogger().log(Level.WARNING, e, () -> "Failed to stop cron jobs.");
        }
    }

    public static CronScheduler create(JavaPlugin plugin) {
        Properties properties = new Properties();
        properties.setProperty("com.aystudio.core.libraries.quartz.scheduler.instanceName", plugin.getName() + "Scheduler");
        properties.setProperty("com.aystudio.core.libraries.quartz.threadPool.threadCount", "20");
        properties.setProperty("com.aystudio.core.libraries.quartz.threadPool.class", "com.aystudio.core.libraries.quartz.simpl.SimpleThreadPool");
        properties.setProperty("com.aystudio.core.libraries.quartz.threadPool.threadPriority", "5");
        properties.setProperty("com.aystudio.core.libraries.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
        properties.setProperty("com.aystudio.core.libraries.quartz.jobStore.class", "com.aystudio.core.libraries.quartz.simpl.RAMJobStore");
        return create(plugin, properties);
    }

    public static CronScheduler create(JavaPlugin plugin, Properties properties) {
        return new CronScheduler(plugin, properties);
    }
}
