package com.mc9y.blank038api.interfaces;

import org.bukkit.event.inventory.InventoryCloseEvent;

@FunctionalInterface
public interface GuiCloseInterface {

    // 要执行的内容就写在这咯
    void execute(InventoryCloseEvent e);
}