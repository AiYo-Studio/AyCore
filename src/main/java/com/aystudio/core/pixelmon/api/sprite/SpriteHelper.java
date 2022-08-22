package com.aystudio.core.pixelmon.api.sprite;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.nms.FMethodClass;
import com.aystudio.core.bukkit.util.common.ReflectionUtil;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import org.bukkit.inventory.ItemStack;


/**
 * 相片辅助类
 *
 * @author Blank038
 */
public class SpriteHelper {
    private Class<?> c;

    public SpriteHelper() {
        try {
            this.c = Class.forName("com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper");
        } catch (Exception ignored) {
            try {
                this.c = Class.forName("com.pixelmonmod.pixelmon.items.SpriteItem");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取某个精灵的精灵相片
     *
     * @param pokemon 目标精灵
     * @return 精灵相片物品
     */
    public ItemStack getSpriteItem(Pokemon pokemon) {
        Object item = ReflectionUtil.invokeMethod(c, "getPhoto", new Class[]{Pokemon.class}, pokemon);
        return ((FMethodClass) AyCore.getInstance().getNMSClass()).convertItemStack((net.minecraft.item.ItemStack) item);
    }
}
