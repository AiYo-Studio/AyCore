package com.mc9y.blank038api.util.common;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.material.MaterialData;

/**
 * @author Blank038
 * @since 2021-10-12
 */
public class EntityUtil {

    public static void createFallingBlock(Block block, Location end, double power) {
        FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), new MaterialData(block.getType()));
        block.setType(Material.AIR);
        fallingBlock.setDropItem(false);
        fallingBlock.setVelocity(end.toVector().subtract(block.getLocation().toVector()));
    }
}
