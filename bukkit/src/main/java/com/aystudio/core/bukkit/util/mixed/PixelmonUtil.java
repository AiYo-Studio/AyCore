package com.aystudio.core.bukkit.util.mixed;

import com.aystudio.core.bukkit.util.common.ReflectionUtil;

/**
 * @author Blank038
 */
public class PixelmonUtil {

    public static void checkAndRun(Runnable successRunnable, Runnable failedRunnable) {
        if (ReflectionUtil.hasClass("com.pixelmonmod.pixelmon.Pixelmon")) {
            successRunnable.run();
        } else if (failedRunnable != null) {
            failedRunnable.run();
        }
    }
}
