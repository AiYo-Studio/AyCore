package com.aystudio.core.bukkit.handler;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.enums.MinecraftVersions;
import com.aystudio.core.bukkit.helper.PlatformHelper;
import com.aystudio.core.bukkit.platform.IPlatformApi;
import com.aystudio.core.bukkit.platform.impl.bukkit.BukkitPlatformApi;
import com.aystudio.core.common.data.CommonData;

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
        // 初始化版本
        CommonData.currentVersion = MinecraftVersions.getVersion();
        CommonData.coreVersion = CommonData.currentVersion.name();
    }

    public static void setPlatform(IPlatformApi platform) {
        AyCore.setPlatformApi(platform);
    }
}
