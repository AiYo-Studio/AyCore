package com.aystudio.core.bukkit.nms.sub;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.nms.INMSClass;
import com.aystudio.core.bukkit.util.key.KeyListener;
import org.bukkit.Bukkit;

/**
 * @author Blank038
 */
public class v1_16_R1 extends INMSClass {

    @Override
    public void registerChannel(AyCore instance) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(instance, "pokemonapi:keyexecute");
        Bukkit.getMessenger().registerIncomingPluginChannel(instance, "pokemonapi:keyexecute", new KeyListener());
    }

    @Override
    public String getVID() {
        return "v1_16_R1";
    }
}
