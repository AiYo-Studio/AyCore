package com.aystudio.core.pixelmon.api.sprite;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.nms.FMethodClass;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import org.bukkit.inventory.ItemStack;

/**
 * 相片辅助类
 *
 * @author Blank038
 */
public class SpriteHelper {

    /**
     * 获取某个精灵的精灵相片
     *
     * @param pokemon 目标精灵
     * @return 精灵相片物品
     */
    public ItemStack getSpriteItem(Pokemon pokemon) {
        return ((FMethodClass) AyCore.getInstance().getNMSClass()).convertItemStack(ItemPixelmonSprite.getPhoto(pokemon));
    }
}
