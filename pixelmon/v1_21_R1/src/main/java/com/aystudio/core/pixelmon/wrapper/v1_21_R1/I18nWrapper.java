package com.aystudio.core.pixelmon.wrapper.v1_21_R1;

import com.aystudio.core.pixelmon.api.i18n.PixelmonI18n;
import com.google.common.collect.ImmutableMap;
import net.minecraft.locale.Language;

import java.io.InputStream;
import java.util.function.BiConsumer;

/**
 * @author Blank038
 */
public class I18nWrapper extends PixelmonI18n {

    public I18nWrapper() {
        this.initializeConsumer = (lang) -> {
            InputStream inputStream = Language.class.getResourceAsStream("/assets/pixelmon/lang/" + lang + ".json");
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            BiConsumer<String, String> biconsumer = builder::put;
            Language.loadFromJson(inputStream, biconsumer);
            this.values.putAll(builder.build());
        };
        this.initialize();
    }
}
