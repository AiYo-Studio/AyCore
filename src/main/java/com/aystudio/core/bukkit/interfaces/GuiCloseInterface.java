package com.aystudio.core.bukkit.interfaces;

import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author Blank038
 */
@FunctionalInterface
public interface GuiCloseInterface {

    /**
     * 要执行的内容就写在这咯
     */
    void execute(InventoryCloseEvent e);
}