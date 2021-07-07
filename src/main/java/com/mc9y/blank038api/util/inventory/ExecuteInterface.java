package com.mc9y.blank038api.util.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Blank038
 */
@FunctionalInterface
public interface ExecuteInterface {

    /**
     * 要执行的内容就写在这咯
     *
     * @param e Inventory 点击事件
     */
    void execute(InventoryClickEvent e);
}
