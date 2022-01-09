package com.aystudio.core.forge.model;

import com.aystudio.core.forge.impl.AbstractForgeListenHandler;
import com.aystudio.core.forge.event.ForgeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import red.mohist.api.event.BukkitHookForgeEvent;


/**
 * @author Blank038
 * @since 2022-01-09
 */
public class MohistModel extends AbstractForgeListenHandler {

    public MohistModel() {
        super("getEvent", "red.mohist.api.event.BukkitHookForgeEvent");
    }

    @EventHandler
    public void onForgeEvent(BukkitHookForgeEvent e) {
        ForgeEvent forgeEvent = new ForgeEvent(e.getEvent());
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }
}
