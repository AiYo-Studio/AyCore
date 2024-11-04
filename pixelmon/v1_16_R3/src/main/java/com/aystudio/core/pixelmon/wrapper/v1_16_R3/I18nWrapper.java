package com.aystudio.core.pixelmon.wrapper.v1_16_R3;

import com.aystudio.core.pixelmon.api.i18n.PixelmonI18n;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.text.LanguageMap;

import java.io.InputStream;
import java.util.function.BiConsumer;

/**
 * @author Blank038
 */
public class I18nWrapper extends PixelmonI18n {

    public I18nWrapper() {
        this.initializeConsumer = (lang) -> {
            InputStream inputStream = LanguageMap.class.getResourceAsStream("/assets/pixelmon/lang/" + lang + ".json");
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            BiConsumer<String, String> biconsumer = builder::put;
            LanguageMap.loadFromJson(inputStream, biconsumer);
            this.values.putAll(builder.build());
        };
    }
}
