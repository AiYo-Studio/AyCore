package com.aystudio.core.bukkit.nms.sub;

import com.aystudio.core.bukkit.nms.INMSClass;
import com.aystudio.core.bukkit.plugin.library.InjectLibraries;
import com.aystudio.core.bukkit.nms.FMethodClass;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * 1.12.2 NMS类
 *
 * @author Blank038
 */
@InjectLibraries(value = {
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0-RC"
})
public class v1_12_R1 extends INMSClass implements FMethodClass {

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

    @Override
    public String getVID() {
        return "v1_12_R1";
    }
}
