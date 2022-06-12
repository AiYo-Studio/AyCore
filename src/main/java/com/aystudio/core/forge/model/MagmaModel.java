package com.aystudio.core.forge.model;

import com.aystudio.core.forge.event.ForgeEvent;
import com.aystudio.core.forge.impl.AbstractForgeListenHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.*;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class MagmaModel extends AbstractForgeListenHandler {

    public MagmaModel() {
        super("getForgeEvents", "org.magmafoundation.magma.api.events.ForgeEvent");
    }

    @EventHandler
    public void onForgeEvent(org.magmafoundation.magma.api.events.ForgeEvent e) {
        ForgeEvent forgeEvent = new ForgeEvent(e.getForgeEvent());
        Bukkit.getPluginManager().callEvent(forgeEvent);
    }
}