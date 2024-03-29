package com.aystudio.core.bukkit.util.common;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * 对玩家操作的工具类
 *
 * @author Blank038
 * @since 2021-03-16
 */
public class PlayerUtil {

    /**
     * 检测玩家是否有指定数量物品
     *
     * @param player   目标玩家
     * @param itemName 目标物品名
     * @param amount   检测数量
     * @return 检测结果
     */
    public static boolean hasItem(Player player, String itemName, int amount, boolean... all) {
        int count = 0, max = (all.length > 0 && all[0] ? player.getInventory().getSize() : 36);
        for (int i = 0; i < max; i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack == null || itemStack.getType() == Material.AIR || !itemStack.hasItemMeta()
                    || !itemStack.getItemMeta().hasDisplayName() || !itemStack.getItemMeta().getDisplayName().equals(itemName)) {
                continue;
            }
            count += itemStack.getAmount();
            if (count >= amount) {
                return true;
            }
        }
        return false;
    }

    /**
     * 扣除玩家背包物品
     *
     * @param player   目标玩家
     * @param itemName 扣除物品名
     * @param amount   扣除数量
     */
    public static void takeItem(Player player, String itemName, int amount, boolean... all) {
        // 判断是否物品存在
        if (PlayerUtil.hasItem(player, itemName, amount)) {
            // 扣除物品
            int need = amount, max = (all.length > 0 && all[0] ? player.getInventory().getSize() : 36);
            for (int i = 0; i < max; i++) {
                if (need <= 0) {
                    break;
                }
                ItemStack itemStack = player.getInventory().getItem(i);
                if (itemStack == null || itemStack.getType() == Material.AIR || !itemStack.hasItemMeta()
                        || !itemStack.getItemMeta().hasDisplayName() || !itemStack.getItemMeta().getDisplayName().equals(itemName)) {
                    continue;
                }
                int itemAmount = itemStack.getAmount(), take = itemAmount > need ? itemAmount - need : itemAmount;
                if (take == itemAmount) {
                    itemStack.setType(Material.AIR);
                } else {
                    itemStack.setAmount(take);
                }
                player.getInventory().setItem(i, itemStack);
                need -= take;
            }
        }
    }
}