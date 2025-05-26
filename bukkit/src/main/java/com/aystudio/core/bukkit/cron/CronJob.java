package com.aystudio.core.bukkit.cron;

import com.aystudio.core.bukkit.cron.context.ContextWrapper;
import org.quartz.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CronJob {
    private static final Map<String, Consumer<ContextWrapper>> JOB_WRAPPER_MAP = new HashMap<>();

    public CronJob(Scheduler scheduler, String identity, String group, String expression, Consumer<ContextWrapper> consumer) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(JobWrapper.class)
                .withIdentity(identity, group)
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(identity, group)
                .withSchedule(CronScheduleBuilder.cronSchedule(expression))
                .build();
        JOB_WRAPPER_MAP.put(jobDetail.getKey().toString(), consumer);
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("成功启动: " + identity);
    }

    public static class JobWrapper implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            String key = context.getJobDetail().getKey().toString();
            Consumer<ContextWrapper> consumer = JOB_WRAPPER_MAP.getOrDefault(key, null);
            System.out.println("Go: " + key);
            if (consumer != null) {
                System.out.println("嗯？");
                consumer.accept(new ContextWrapper(context));
            }
        }
    }
}
