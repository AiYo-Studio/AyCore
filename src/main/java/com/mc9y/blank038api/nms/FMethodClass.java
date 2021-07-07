package com.mc9y.blank038api.nms;

import org.bukkit.inventory.ItemStack;

/**
 * Forge 方法抽象类, 用于 NMS 类继承
 *
 * @author Blank038
 */
public interface FMethodClass {

    ItemStack convertItemStack(net.minecraft.item.ItemStack itemStack);
}
