package com.aystudio.core.pixelmon;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.util.common.ReflectionUtil;
import com.aystudio.core.bukkit.util.custom.LoggerUtil;
import com.aystudio.core.common.link.ILink;
import com.aystudio.core.forge.IForgeListenHandler;
import com.aystudio.core.pixelmon.api.enums.EnumPixelmon;
import com.aystudio.core.pixelmon.api.enums.PixelmonVersionEnum;
import com.aystudio.core.pixelmon.api.i18n.PixelmonI18n;
import com.aystudio.core.pixelmon.api.sprite.SpriteHelper;
import com.aystudio.core.pixelmon.api.stats.StatsHelper;
import com.aystudio.core.pixelmon.wrapper.IPokemonApiWrapper;
import com.pixelmonmod.pixelmon.Pixelmon;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

/**
 * @author Blank038
 */
@Getter
public class PokemonAPI implements ILink {
    @Getter
    private static PokemonAPI instance;

    private IPokemonApiWrapper pokemonApiWrapper;
    private IForgeListenHandler forgeListener;
    @Setter
    private PixelmonI18n language;
    @Setter
    private SpriteHelper spriteHelper;
    @Setter
    private StatsHelper statsHelper;
    private EnumPixelmon enumPixelmon;
    private PixelmonVersionEnum pixelmonVersion;

    public PokemonAPI() {
        instance = this;
    }

    public void onLoad() {
        switch ((enumPixelmon = getPixelmonType())) {
            case PIXELMON_REFORGED:
                this.setup();
                LoggerUtil.getOrRegister(AyCore.class).log("&fPixelmonReforged 版本: &a" + this.getVersion(EnumPixelmon.PIXELMON_REFORGED));
                LoggerUtil.getOrRegister(AyCore.class).log("&f成功加载: &aPokemonAPI");
                break;
            case NONE:
            default:
                break;
        }
    }

    private EnumPixelmon getPixelmonType() {
        if (ReflectionUtil.hasClass("com.pixelmonmod.pixelmon.Pixelmon")) {
            return EnumPixelmon.PIXELMON_REFORGED;
        }
        return EnumPixelmon.NONE;
    }

    public void setup() {
        String version = "未知";
        try {
            version = Bukkit.getServer().getClass().getPackage().toString().replace(".", ",").split(",")[3];
        } catch (Exception ignored) {
        }
        String path = null;
        switch (version) {
            case "v1_12_R1":
                path = version;
                this.pixelmonVersion = PixelmonVersionEnum.v1_12_R1;
                break;
            case "v1_16_R3":
                path = version;
                this.pixelmonVersion = PixelmonVersionEnum.v1_16_R3;
                break;
            default:
                break;
        }
        if (path == null) {
            return;
        }
        try {
            Class<?> classz = Class.forName("com.aystudio.core.pixelmon.wrapper." + path + ".PokemonApiWrapper");
            pokemonApiWrapper = (IPokemonApiWrapper) classz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getVersion(EnumPixelmon enumPixelmon) {
        String version;
        try {
            Class<?> c = Class.forName(enumPixelmon.getPackage());
            Field field = c.getDeclaredField("VERSION");
            field.setAccessible(true);
            version = (String) field.get(null);
            return version;
        } catch (Exception e) {
            version = Pixelmon.getVersion();
        }
        return version;
    }
}