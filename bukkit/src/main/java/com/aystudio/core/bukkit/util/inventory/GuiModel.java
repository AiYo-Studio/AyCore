package com.aystudio.core.bukkit.util.inventory;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.interfaces.GuiCloseInterface;
import com.aystudio.core.bukkit.util.common.TextUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
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
import java.util.function.Consumer;

/**
 * @author Blank038
 */
@SuppressWarnings("unused")
public class GuiModel implements InventoryHolder, Listener {
    /**
     * -- GETTER --
     * 获取界面标题
     */
    @Getter
    private final String title;
    /**
     * 创建的 Inventory
     */
    private final Inventory inventory;
    /**
     * 点击执行器
     */
    private Consumer<InventoryClickEvent> clickConsumer;
    /**
     * 关闭执行器
     */
    private Consumer<InventoryCloseEvent> closeConsumer;
    /**
     * -- SETTER --
     * 设置是否关闭销毁
     */
    @Setter
    private boolean closeRemove, listener;

    /**
     * 初始化 GuiModel
     *
     * @param title 界面标题
     * @param size  界面大小
     */
    public GuiModel(String title, int size) {
        this.title = TextUtil.formatHexColor(title);
        this.inventory = Bukkit.createInventory(this, size, title);
        this.closeRemove = true;
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
     * 设置点击执行内容
     *
     * @param executeInterface 点击执行器
     */
    @Deprecated
    public void execute(ExecuteInterface executeInterface) {
        this.clickConsumer = executeInterface::execute;
    }

    public void onClick(Consumer<InventoryClickEvent> consumer) {
        this.clickConsumer = consumer;
    }

    /**
     * 设置关闭执行器
     *
     * @param guiCloseInterface 关闭执行器
     */
    @Deprecated
    public void setCloseInterface(GuiCloseInterface guiCloseInterface) {
        this.closeConsumer = guiCloseInterface::execute;
    }

    public void onClose(Consumer<InventoryCloseEvent> consumer) {
        this.closeConsumer = consumer;
    }

    /**
     * 界面关闭执行
     *
     * @param event 关闭事件
     */
    private void onGuiClose(InventoryCloseEvent event) {
        if (closeConsumer != null) {
            closeConsumer.accept(event);
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
        if (clickConsumer == null) {
            return;
        }
        clickConsumer.accept(e);
    }

    /**
     * 获取创建的界面
     *
     * @return 目标Inventory
     */
    @Override
    public @NonNull Inventory getInventory() {
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