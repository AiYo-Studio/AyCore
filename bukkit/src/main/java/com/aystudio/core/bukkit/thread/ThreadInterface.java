package com.aystudio.core.bukkit.thread;

/**
 * @author Blank038
 */
public interface ThreadInterface extends Runnable {

    /**
     * 执行代码
     */
    @Override
    void run();

    /**
     * 取消线程
     */
    void cancel();

    /**
     * 获取线程是否被关闭
     *
     * @return 是否取消
     */
    boolean isCancel();
}
