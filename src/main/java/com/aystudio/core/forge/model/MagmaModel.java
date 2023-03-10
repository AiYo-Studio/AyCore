package com.aystudio.core.forge.model;

import com.aystudio.core.forge.event.ForgeEvent;
import com.aystudio.core.forge.impl.AbstractForgeListenHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.magmafoundation.magma.api.events.ForgeEvents;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class MagmaModel extends AbstractForgeListenHandler {

    public MagmaModel() {
        super("getForgeEvents", "org.magmafoundation.magma.api.events.ForgeEvents");
    }

    @EventHandler
    public void onForgeEvent(ForgeEvents e) {
        ForgeEvent forgeEvent = new ForgeEvent(e.getForgeEvents());
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }
}