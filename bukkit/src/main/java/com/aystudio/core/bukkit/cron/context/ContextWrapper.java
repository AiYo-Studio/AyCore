package com.aystudio.core.bukkit.cron.context;

import org.quartz.*;

import java.util.Date;

public class ContextWrapper {
    private final JobExecutionContext context;

    public ContextWrapper(JobExecutionContext context) {
        this.context = context;
    }

    public Scheduler getScheduler() {
        return context.getScheduler();
    }

    public Trigger getTrigger() {
        return context.getTrigger();
    }

    public Calendar getCalendar() {
        return context.getCalendar();
    }

    public boolean isRecovering() {
        return context.isRecovering();
    }

    public TriggerKey getRecoveringTriggerKey() throws IllegalStateException {
        return context.getRecoveringTriggerKey();
    }

    public int getRefireCount() {
        return context.getRefireCount();
    }

    public JobDataMap getMergedJobDataMap() {
        return context.getMergedJobDataMap();
    }

    public JobDetail getJobDetail() {
        return context.getJobDetail();
    }

    public Job getJobInstance() {
        return context.getJobInstance();
    }

    public Date getFireTime() {
        return context.getFireTime();
    }

    public Date getScheduledFireTime() {
        return context.getScheduledFireTime();
    }

    public Date getPreviousFireTime() {
        return context.getPreviousFireTime();
    }

    public Date getNextFireTime() {
        return context.getNextFireTime();
    }

    public String getFireInstanceId() {
        return context.getFireInstanceId();
    }

    public Object getResult() {
        return context.getResult();
    }

    public void setResult(Object result) {
        this.context.setResult(result);
    }

    public long getJobRunTime() {
        return context.getJobRunTime();
    }

    public void put(Object key, Object value) {
        this.context.put(key, value);
    }

    public Object get(Object obj) {
        return this.context.get(obj);
    }
}
