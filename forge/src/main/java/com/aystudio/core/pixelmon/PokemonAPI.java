package com.aystudio.core.pixelmon;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.common.ReflectionUtil;
import com.aystudio.core.bukkit.util.custom.LoggerUtil;
import com.aystudio.core.common.link.ILink;
import com.aystudio.core.forge.IForgeListenHandler;
import com.aystudio.core.pixelmon.api.enums.EnumPixelmon;
import com.aystudio.core.pixelmon.api.i18n.PixelmonI18n;
import com.aystudio.core.pixelmon.api.sprite.SpriteHelper;
import com.aystudio.core.pixelmon.api.stats.StatsHelper;
import com.pixelmonmod.pixelmon.Pixelmon;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

/**
 * @author Blank038
 */
@SuppressWarnings("unused")
public class PokemonAPI implements ILink {
    @Getter
    private static PokemonAPI instance;
    public static boolean old;


    @Getter
    private IForgeListenHandler forgeListener;
    private PixelmonI18n lang;
    private SpriteHelper sh;
    @Getter
    private StatsHelper statsHelper;
    @Getter
    private EnumPixelmon enumPixelmon;

    public PokemonAPI() {
        instance = this;
    }

    public void onLoad() {
        switch ((enumPixelmon = getPixelmonType())) {
            case PIXELMON_REFORGED:
                this.setupLang();
                statsHelper = new StatsHelper(this);
                sh = new SpriteHelper();
                LoggerUtil.getOrRegister(AyCore.class).log("&fPixelmonReforged 版本: &a" + this.getVersion(EnumPixelmon.PIXELMON_REFORGED));
                LoggerUtil.getOrRegister(AyCore.class).log("&f成功加载: &aPokemonAPI");
                break;
            case NONE:
            default:
                break;
        }
    }

    @Override
    public void updateField(String field, Object value) {
        switch (field) {
            case "old":
                old = (boolean) value;
                break;
            default:
                break;
        }
    }

    /**
     * 获取是否为宝可梦环境加载
     *
     * @return 是否为宝可梦环境
     */
    @Deprecated
    public boolean isPixelmon() {
        return enumPixelmon != EnumPixelmon.NONE;
    }

    /**
     * 获得 Pixelmon 语言类
     *
     * @return 语言类
     */
    public PixelmonI18n getLanguage() {
        return lang;
    }

    /**
     * 获取相片辅助类
     *
     * @return 相片辅助类
     */
    public SpriteHelper getSpriteHelper() {
        return sh;
    }

    private EnumPixelmon getPixelmonType() {
        if (ReflectionUtil.hasClass("com.pixelmonmod.pixelmon.Pixelmon")) {
            return EnumPixelmon.PIXELMON_REFORGED;
        }
        return EnumPixelmon.NONE;
    }

    public void setupLang() {
        String version = "未知";
        try {
            version = Bukkit.getServer().getClass().getPackage().toString().replace(".", ",").split(",")[3];
        } catch (Exception ignored) {
        }
        switch (version) {
            case "v1_7_R4":
                lang = new PixelmonI18n(AyCore.getInstance(), false);
                break;
            case "v1_12_R1":
                lang = new PixelmonI18n(AyCore.getInstance(), true);
                break;
            default:
                break;
        }
    }

    public String getVersion(EnumPixelmon enumPixelmon) {
        String version;
        try {
            Class<?> c = Class.forName(enumPixelmon.getPackage());
            Field field = c.getDeclaredField("VERSION");
            version = (String) field.get(null);
            return version;
        } catch (Exception e) {
            version = Pixelmon.getVersion();
        }
        return version;
    }
}