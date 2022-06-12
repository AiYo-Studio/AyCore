package com.aystudio.core.forge.model;

import com.aystudio.core.forge.event.ForgeEvent;
import com.aystudio.core.forge.impl.AbstractForgeListenHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.*;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class CatServerModel extends AbstractForgeListenHandler {

    public CatServerModel() {
        super("getForgeEvent", "catserver.api.bukkit.event.ForgeEvent");
    }

    @EventHandler
    public void onListener(catserver.api.bukkit.event.ForgeEvent event) {
        ForgeEvent forgeEvent = new ForgeEvent(event.getForgeEvent());
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }
}