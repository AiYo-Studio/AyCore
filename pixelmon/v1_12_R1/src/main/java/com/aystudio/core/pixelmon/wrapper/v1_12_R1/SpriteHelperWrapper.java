package com.aystudio.core.pixelmon.wrapper.v1_12_R1;

import com.aystudio.core.pixelmon.api.sprite.SpriteHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @author Blank038
 */
public class SpriteHelperWrapper extends SpriteHelper {

    @Override
    public ItemStack getSpriteItem(Pokemon pokemon) {
        net.minecraft.item.ItemStack itemStack = ItemPixelmonSprite.getPhoto(pokemon);
        net.minecraft.server.v1_12_R1.ItemStack craftItemStack = (net.minecraft.server.v1_12_R1.ItemStack) (Object) itemStack;
        return CraftItemStack.asBukkitCopy(craftItemStack);
    }
}
