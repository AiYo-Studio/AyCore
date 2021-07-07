package com.mc9y.blank038api.nms.sub;

import com.mc9y.blank038api.Blank038API;
import com.mc9y.blank038api.nms.INMSClass;
import com.mc9y.blank038api.util.key.KeyListener;
import org.bukkit.Bukkit;

/**
 * @author Blank038
 */
public class v1_16_R1 extends INMSClass {

    @Override
    public void registerChannel(Blank038API instance) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(instance, "pokemonapi:keyexecute");
        Bukkit.getMessenger().registerIncomingPluginChannel(instance, "pokemonapi:keyexecute", new KeyListener());
    }

    @Override
    public String getVID() {
        return "v1_16_R1";
    }
}
