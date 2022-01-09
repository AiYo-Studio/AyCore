package com.aystudio.core.forge.model;

import catserver.api.bukkit.event.ForgeEvent;
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
    public void onListener(ForgeEvent event) {
        com.aystudio.core.forge.event.ForgeEvent forgeEvent = new com.aystudio.core.forge.event.ForgeEvent(event.getForgeEvent());
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }
}