package com.aystudio.core.bukkit.platform.impl.bukkit.warpper;

import com.aystudio.core.bukkit.platform.wrapper.ITaskWrapper;
import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Blank038
 */
@AllArgsConstructor
public class BukkitTaskWrapper implements ITaskWrapper {

    public final BukkitTask bukkitTask;

    @Override
    public void cancel() {
        bukkitTask.cancel();
    }
}
