package com.aystudio.core.bukkit.handler;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.helper.PlatformHelper;
import com.aystudio.core.bukkit.platform.IPlatformApi;
import com.aystudio.core.bukkit.platform.impl.bukkit.BukkitPlatformApi;

import java.util.logging.Level;

/**
 * @author Blank038
 */
public class PlatformHandler {

    public static void initPlatform() {
        if (PlatformHelper.isFolia()) {
            try {
                Class<? extends IPlatformApi> classes = (Class<? extends IPlatformApi>) Class.forName("com.aystudio.core.bukkit.platform.folia.FoliaPlatformApi");
                setPlatform(classes.newInstance());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                AyCore.getInstance().getLogger().log(Level.SEVERE, "Failed to initialize platform for Folia");
            }
        } else {
            setPlatform(new BukkitPlatformApi());
        }
    }

    public static void setPlatform(IPlatformApi platform) {
        AyCore.setPlatformApi(platform);
    }
}
