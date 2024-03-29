package com.aystudio.core.forge;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.common.ReflectionUtil;
import com.aystudio.core.bukkit.util.custom.LoggerUtil;
import com.aystudio.core.common.link.ILink;
import com.aystudio.core.forge.model.CatServerModel;
import com.aystudio.core.forge.model.MagmaModel;
import com.aystudio.core.forge.model.MohistModel;
import lombok.Getter;
import org.bukkit.Bukkit;

/**
 * @author Blank038
 * @since 2022-01-09
 */
public class ForgeInject implements ILink {
    @Getter
    private static ForgeInject instance;
    @Getter
    private IForgeListenHandler forgeListener;

    public ForgeInject() {
        instance = this;
        // 注册 Forge 事件监听
        boolean forward = AyCore.getInstance().getConfig().getBoolean("forward_forge_event", true);
        if (forward && ReflectionUtil.hasClass("catserver.api.bukkit.event.ForgeEvent")) {
            Bukkit.getPluginManager().registerEvents((forgeListener = new CatServerModel()), AyCore.getInstance());
            LoggerUtil.getOrRegister(AyCore.class).log("&f载入挂钩核心: §aCatServer");
        } else if (forward && ReflectionUtil.hasClass("red.mohist.api.event.BukkitHookForgeEvent")) {
            Bukkit.getPluginManager().registerEvents((forgeListener = new MohistModel()), AyCore.getInstance());
            LoggerUtil.getOrRegister(AyCore.class).log("&f载入挂钩核心: §aMohist");
        } else if (forward && ReflectionUtil.hasClass("org.magmafoundation.magma.api.events.ForgeEvents")) {
            Bukkit.getPluginManager().registerEvents((forgeListener = new MagmaModel()), AyCore.getInstance());
            LoggerUtil.getOrRegister(AyCore.class).log("&f载入挂钩核心: §aMagma");
        } else {
            LoggerUtil.getOrRegister(AyCore.class).log("&f无挂钩核心载入");
        }
    }

    @Override
    public void onLoad() {

    }
}
