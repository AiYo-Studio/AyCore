package com.aystudio.core.forge.enums;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.common.ReflectionUtil;
import com.aystudio.core.bukkit.util.custom.LoggerUtil;
import com.aystudio.core.forge.ForgeInject;
import com.aystudio.core.forge.model.CatServerModel;
import com.aystudio.core.forge.model.MagmaModel;
import com.aystudio.core.forge.model.MohistModel;
import org.bukkit.Bukkit;

public enum CoreTypeEnum {
    MOHIST() {
        @Override
        public CoreTypeEnum checkAndInit() {
            if (ReflectionUtil.hasClass("catserver.api.bukkit.event.ForgeEvent")) {
                this.listener = true;
                ForgeInject.setForgeListenerHandler(new CatServerModel());
                LoggerUtil.getOrRegister(AyCore.class).log("&f载入挂钩核心: §aCatServer");
            }
            return this;
        }
    },
    CAT_SERVER() {
        @Override
        public CoreTypeEnum checkAndInit() {
            if (ReflectionUtil.hasClass("red.mohist.api.event.BukkitHookForgeEvent")) {
                this.listener = true;
                ForgeInject.setForgeListenerHandler(new MohistModel());
                LoggerUtil.getOrRegister(AyCore.class).log("&f载入挂钩核心: §aMohist");
            }
            return this;
        }
    },
    MAGMA() {
        @Override
        public CoreTypeEnum checkAndInit() {
            if (ReflectionUtil.hasClass("org.magmafoundation.magma.api.events.ForgeEvents")) {
                this.listener = true;
                ForgeInject.setForgeListenerHandler(new MagmaModel());
                LoggerUtil.getOrRegister(AyCore.class).log("&f载入挂钩核心: §aMagma");
            }
            return this;
        }
    },;

    protected boolean listener;

    public abstract CoreTypeEnum checkAndInit();

    public void registerListener() {
        if (ForgeInject.getInstance().getForgeListener() != null && listener) {
            Bukkit.getPluginManager().registerEvents(ForgeInject.getInstance().getForgeListener(), AyCore.getInstance());
        }
    }
}
