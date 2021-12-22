package com.aystudio.core.pixelmon.api.sprite;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.nms.FMethodClass;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import org.bukkit.inventory.ItemStack;

/**
 * 相片辅助类
 *
 * @author Blank038
 */
public class SpriteHelper {
    // private List<EnumSpecies> mfSprite = ImmutableList.of(EnumSpecies.Hippopotas, EnumSpecies.Hippowdon, EnumSpecies.Unfezant, EnumSpecies.Frillish, EnumSpecies.Jellicent, EnumSpecies.Pyroar, EnumSpecies.Wobbuffet, EnumSpecies.Combee);

    /**
     * 获取某个精灵的精灵相片
     *
     * @param pokemon 目标精灵
     * @return 精灵相片物品
     */
    public ItemStack getSpriteItem(Pokemon pokemon) {
///        net.minecraft.server.v1_12_R1.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
//        NBTTagCompound compound = nmsitem.hasTag() ? nmsitem.getTag() : new NBTTagCompound();
//        assert compound != null;
//        compound.set("SpriteName", new NBTTagString(getSprite(pokemon)));
//        nmsitem.setTag(compound);
///        item = CraftItemStack.asBukkitCopy(nmsitem);
        return ((FMethodClass) AyCore.getBlank038API().getNMSClass()).convertItemStack(ItemPixelmonSprite.getPhoto(pokemon));
    }

///    /*
//     * 获取某个精灵的相片路径
//     * 源自 PixelmonReforged Mod
//     */
//    public String getSprite(Pokemon pixelmon) {
//        String filePath = "pixelmon:sprites/";
//        EnumSpecialTexture specialTexture = pixelmon.getSpecialTexture();
//        if (((pixelmon.getFormEnum() instanceof EnumNoForm)) && (pixelmon.getFormEnum().isTemporary())) {
//            specialTexture = EnumSpecialTexture.None;
//        }
//        if (((pixelmon.getFormEnum() instanceof EnumPrimal)) && (pixelmon.getFormEnum().isTemporary())) {
//            specialTexture = EnumSpecialTexture.None;
//        }
//        if (pixelmon.isShiny()) {
//            filePath = filePath + "shiny";
//            specialTexture = EnumSpecialTexture.None;
//        }
//        filePath = filePath + "pokemon/" + pixelmon.getSpecies().getNationalPokedexNumber() +
//                getSpriteExtra(pixelmon.getSpecies().name, pixelmon.getForm(), pixelmon.getGender(), specialTexture.ordinal(), pixelmon.isShiny());
//        return filePath;
//    }
//
//    /*
//     * 获取某个精灵的相片路径
//     * 源自 PixelmonReforged Mod
//     */
//    public String getSpriteExtra(String name, int form, Gender gender, int specialTexture, boolean shiny) {
//        Optional<EnumSpecies> optional = EnumSpecies.getFromName(name);
//        if (optional.isPresent()) {
//            EnumSpecies pokemon = optional.get();
//            IEnumForm ief = pokemon.getFormEnum(form);
//            if (ief != EnumNoForm.NoForm) {
//                return PokemonAPI.v_730 ? ief.getSpriteSuffix(shiny) : getFormEnum(ief);
//            }
//            if (mfSprite.contains(pokemon)) {
//                return "-" + gender.name().toLowerCase();
//            }
//            if (specialTexture > 0 && pokemon.hasSpecialTexture()) {
//                return "-special";
//            }
//        }
//        return "";
//    }
//
//    private String getFormEnum(IEnumForm c) {
//        try {
//            Class<? extends IEnumForm> z = c.getClass();
//            Method method = z.getMethod("getSpriteSuffix");
//            return (String) method.invoke(c);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
///    }
}
