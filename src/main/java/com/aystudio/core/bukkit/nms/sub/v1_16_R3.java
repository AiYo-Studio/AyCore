package com.aystudio.core.bukkit.nms.sub;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.nms.FMethodClass;
import com.aystudio.core.bukkit.nms.INMSClass;
import com.aystudio.core.bukkit.util.key.KeyListener;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * @author Blank038
 */
public class v1_16_R3 extends INMSClass implements FMethodClass {

    @Override
    public void registerChannel(AyCore instance) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(instance, "pokemonapi:keyexecute");
        Bukkit.getMessenger().registerIncomingPluginChannel(instance, "pokemonapi:keyexecute", new KeyListener());
    }

    @Override
    public String getVID() {
        return "v1_16_R3";
    }

    /**
     * 将 net.minecraft.item.ItemStack 转换为 org.bukkit.inventory.ItemStack
     *
     * @param itemStack net.minecraft.item.ItemStack 对象
     * @return org.bukkit.inventory.ItemStack 实例
     */
    @Override
    public ItemStack convertItemStack(net.minecraft.item.ItemStack itemStack) {
        return org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asBukkitCopy(itemStack);
    }
}
