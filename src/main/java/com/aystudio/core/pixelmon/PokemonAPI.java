package com.aystudio.core.pixelmon;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.common.ReflectionUtil;
import com.aystudio.core.bukkit.util.custom.LoggerUtil;
import com.aystudio.core.pixelmon.api.enums.EnumPixelmon;
import com.aystudio.core.pixelmon.api.i18n.PixelmonI18n;
import com.aystudio.core.pixelmon.api.sprite.SpriteHelper;
import com.aystudio.core.pixelmon.api.stats.StatsHelper;
import com.aystudio.core.forge.IForgeListenHandler;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

/**
 * @author Blank038
 */
@SuppressWarnings("unused")
public class PokemonAPI {
    public static boolean old;
    private IForgeListenHandler forgeListener;
    private PixelmonI18n lang;
    private SpriteHelper sh;
    private StatsHelper statsHelper;
    private EnumPixelmon enumPixelmon;

    public void onLoad() {
        switch ((enumPixelmon = getPixelmonType())) {
            case PIXELMON_REFORGED:
                this.setupLang();
                statsHelper = new StatsHelper(this);
                sh = new SpriteHelper();
                LoggerUtil.getOrRegister(AyCore.class).log("&fPixelmonReforged 版本: &a" + getVersion(EnumPixelmon.PIXELMON_REFORGED));
                LoggerUtil.getOrRegister(AyCore.class).log("&f成功加载: &aPokemonAPI");
                break;
            case PIXELMON_GENERATIONS:
                this.setupLang();
                LoggerUtil.getOrRegister(AyCore.class).log("&fPixelmonGenerations 版本: &a" + getVersion(EnumPixelmon.PIXELMON_GENERATIONS));
                LoggerUtil.getOrRegister(AyCore.class).log("&f成功加载: &aPokemonAPI");
                break;
            case NONE:
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

    public IForgeListenHandler getForgeListener() {
        return this.forgeListener;
    }

    /**
     * 获取当前 Pixelmon 目标类型
     * 仅会检测: PixelmonReforged(重铸), PixelmonGenerations(世代), None(无)
     *
     * @return Pixelmon 目标类型
     */
    public EnumPixelmon getEnumPixelmon() {
        return enumPixelmon;
    }

    private EnumPixelmon getPixelmonType() {
        if (ReflectionUtil.hasClass("com.pixelmonmod.pixelmon.Pixelmon")) {
            return EnumPixelmon.PIXELMON_REFORGED;
        } else if (ReflectionUtil.hasClass("com.pixelmongenerations.core.Pixelmon")) {
            return EnumPixelmon.PIXELMON_GENERATIONS;
        }
        return EnumPixelmon.NONE;
    }

    /**
     * 获取 Pokemon 信息处理类
     *
     * @return Pokemon 信息处理类
     */
    public StatsHelper getStatsHelper() {
        return statsHelper;
    }

    public void setupLang() {
        String version = "未知";
        try {
            version = Bukkit.getServer().getClass().getPackage().toString().replace(".", ",").split(",")[3];
        } catch (Exception ignored) {
        }
        switch (version) {
            case "v1_7_R4":
                lang = new PixelmonI18n(AyCore.getBlank038API(), false);
                break;
            case "v1_12_R1":
                lang = new PixelmonI18n(AyCore.getBlank038API(), true);
                break;
            default:
                break;
        }
    }

    public String getVersion(EnumPixelmon enumPixelmon) {
        String version = "获取失败";
        try {
            Class<?> c = Class.forName(enumPixelmon.getPackage());
            Field field = c.getField("VERSION");
            version = (String) field.get(c);
            return version;
        } catch (Exception ignored) {
        }
        return version;
    }
}