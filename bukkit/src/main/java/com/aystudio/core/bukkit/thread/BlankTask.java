package com.aystudio.core.bukkit.thread;

/**
 * @author Blank038
 */
@Deprecated
public class BlankTask extends Thread {
    private final BlankThread TARGET_THREAD;
    private final int TASK_ID;

    protected BlankTask(BlankThread targetThread, int id) {
        this.TARGET_THREAD = targetThread;
        this.TASK_ID = id;
    }

    @Override
    public void run() {
        if (TARGET_THREAD == null) {
            return;
        }
        while (true) {
            if (TARGET_THREAD.isCancel()) {
                ThreadProcessor.THREAD_MAP.remove(TASK_ID);
                break;
            }
            ThreadProcessor.THREAD_MAP.get(TASK_ID).addTick();
        }
    }
}
