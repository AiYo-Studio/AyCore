package com.aystudio.core.pixelmon.wrapper.v1_12_R1;

import com.aystudio.core.pixelmon.api.i18n.PixelmonI18n;
import net.minecraft.util.text.translation.LanguageMap;

import java.io.InputStream;

/**
 * @author Blank038
 */
public class I18nWrapper extends PixelmonI18n {

    public I18nWrapper() {
        this.initializeConsumer = (lang) -> {
            InputStream inputStream = LanguageMap.class.getResourceAsStream("/assets/pixelmon/lang/" + lang + ".lang");
            values.putAll(LanguageMap.parseLangFile(inputStream));
        };
        this.initialize();
    }
}
