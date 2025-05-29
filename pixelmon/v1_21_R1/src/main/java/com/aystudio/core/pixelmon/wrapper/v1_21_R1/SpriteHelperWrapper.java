package com.aystudio.core.pixelmon.wrapper.v1_21_R1;

import com.aystudio.core.bukkit.util.common.ReflectionUtil;
import com.aystudio.core.pixelmon.api.sprite.SpriteHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @author Blank038
 */
public class SpriteHelperWrapper extends SpriteHelper {
    private Class<?> c;

    public SpriteHelperWrapper() {
        try {
            this.c = Class.forName("com.pixelmonmod.pixelmon.api.util.helpers.SpriteItemHelper");
        } catch (ClassNotFoundException ignored) {
            try {
                this.c = Class.forName("com.pixelmonmod.pixelmon.items.SpriteItem");
            } catch (ClassNotFoundException ignore) {
            }
        }
    }

    @Override
    public ItemStack getSpriteItem(Pokemon pokemon) {
        Object item = ReflectionUtil.invokeMethod(c, "getPhoto", new Class[]{Pokemon.class}, pokemon);
        return CraftItemStack.asBukkitCopy((net.minecraft.world.item.ItemStack) item);
    }
}
