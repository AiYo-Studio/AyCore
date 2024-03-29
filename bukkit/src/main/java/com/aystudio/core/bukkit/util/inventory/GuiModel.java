package com.aystudio.core.bukkit.util.inventory;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.interfaces.GuiCloseInterface;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Blank038
 */
@SuppressWarnings("unused")
public class GuiModel implements InventoryHolder, Listener {
    /**
     * 创建的 Inventory
     */
    private final Inventory inventory;
    /**
     * 点击执行器
     */
    private ExecuteInterface executeInterface;
    /**
     * 关闭执行器
     */
    private GuiCloseInterface guiCloseInterface;
    private boolean closeRemove, listener;

    /**
     * 初始化 GuiModel
     *
     * @param title 界面标题
     * @param size  界面大小
     */
    public GuiModel(String title, int size) {
        inventory = Bukkit.createInventory(this, size, title.replace("&", "§"));
        closeRemove = true;
    }

    /**
     * 设置物品 (组)
     *
     * @param itemMap 物品列表
     */
    public void setItem(HashMap<Integer, ItemStack> itemMap) {
        for (Map.Entry<Integer, ItemStack> entry : itemMap.entrySet()) {
            setItem(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 让玩家打开该界面
     *
     * @param player 目标玩家
     */
    public void openInventory(Player player) {
        player.openInventory(getInventory());
    }

    /**
     * 是否在关闭时释放界面
     *
     * @return 是否释放
     */
    public boolean closeRemove() {
        return closeRemove;
    }

    /**
     * 设置是否关闭销毁
     *
     * @param remove 是否销毁
     */
    public void setCloseRemove(boolean remove) {
        closeRemove = remove;
    }

    /**
     * 设置物品
     *
     * @param slot      槽位
     * @param itemStack 物品
     */
    public void setItem(Integer slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    /**
     * 设置是否启用监听
     *
     * @param listener 是否监听
     */
    @Deprecated
    public void setListener(boolean listener) {
        this.registerListener(AyCore.getInstance());
    }

    /**
     * 设置是否启用监听
     *
     * @param plugin Plugin 对象
     */
    public void registerListener(Plugin plugin) {
        if (!this.listener) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
        this.listener = true;
    }

    public synchronized void unregisterListener(boolean close) {
        if (this.listener) {
            HandlerList.unregisterAll(this);
        }
        // 关闭界面
        if (close) {
            Iterator<HumanEntity> iterator = inventory.getViewers().iterator();
            while (iterator.hasNext()) {
                HumanEntity humanEntity = iterator.next();
                if (humanEntity.getOpenInventory().getTopInventory() == this.inventory) {
                    humanEntity.closeInventory();
                }
            }
        }
    }

    /**
     * 获取界面标题
     *
     * @return Inventory 的 Title
     */
    public String getTitle() {
        return inventory.getTitle();
    }

    /**
     * 设置点击执行内容
     *
     * @param executeInterface 点击执行器
     */
    public void execute(ExecuteInterface executeInterface) {
        this.executeInterface = executeInterface;
    }

    /**
     * 设置关闭执行器
     *
     * @param guiCloseInterface 关闭执行器
     */
    public void setCloseInterface(GuiCloseInterface guiCloseInterface) {
        this.guiCloseInterface = guiCloseInterface;
    }

    /**
     * 界面关闭执行
     *
     * @param event 关闭事件
     */
    private void onGuiClose(InventoryCloseEvent event) {
        if (guiCloseInterface != null) {
            guiCloseInterface.execute(event);
        }
        if (closeRemove) {
            this.unregisterListener(false);
        }
    }

    /**
     * 点击界面
     *
     * @param e 开始执行
     */
    private void run(InventoryClickEvent e) {
        if (executeInterface == null) {
            return;
        }
        executeInterface.execute(e);
    }

    /**
     * 获取创建的界面
     *
     * @return 目标Inventory
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == this) {
            this.run(e);
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == this) {
            this.onGuiClose(e);
        }
    }
}