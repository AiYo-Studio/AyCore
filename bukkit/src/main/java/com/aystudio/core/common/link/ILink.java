package com.aystudio.core.common.link;

import com.aystudio.core.bukkit.AyCore;

import java.util.logging.Level;

public interface ILink {

    void onLoad();

    default void updateField(String field, Object value) {
    }

    static ILink newLink(String classz) {
        try {
            return (ILink) Class.forName(classz).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            AyCore.getInstance().getLogger().log(Level.SEVERE, e, () -> "初始化 com.aystudio.core.pixelmon.PokemonAPI 接口出现异常");
        }
        return null;
    }
}
