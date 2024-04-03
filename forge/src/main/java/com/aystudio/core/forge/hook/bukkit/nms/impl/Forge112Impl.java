package com.aystudio.core.forge.hook.bukkit.nms.impl;

import com.aystudio.core.bukkit.nms.sub.v1_12_R1;
import com.aystudio.core.forge.hook.bukkit.nms.FMethodClass;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @author Blank038
 */
public class Forge112Impl extends v1_12_R1 implements FMethodClass {

    @Override
    public String getVID() {
        return super.getVID() + "-Forge";
    }

    /**
     * 将 net.minecraft.item.ItemStack 转换为 org.bukkit.inventory.ItemStack
     *
     * @param itemStack net.minecraft.item.ItemStack 对象
     * @return org.bukkit.inventory.ItemStack 实例
     */
    @Override
    public ItemStack convertItemStack(net.minecraft.item.ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack craftItemStack = (net.minecraft.server.v1_12_R1.ItemStack) (Object) itemStack;
        return CraftItemStack.asBukkitCopy(craftItemStack);
    }
}
