package com.aystudio.core.bukkit.thread;

/**
 * @author Blank038
 */
@Deprecated
public class BlankThread implements ThreadInterface {
    private boolean isCancel;
    private final int tick;
    private int delay;

    public BlankThread(int tick) {
        this.tick = tick;
    }

    public void addTick() {
        delay++;
        if (delay == tick) {
            run();
            delay = 0;
        }
        sleep(50L);
    }

    @Override
    public void run() {

    }

    @Override
    public final void cancel() {
        isCancel = true;
    }

    @Override
    public boolean isCancel() {
        return isCancel;
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}