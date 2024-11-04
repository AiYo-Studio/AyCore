package com.aystudio.core.pixelmon.api.enums;

public enum EnumPixelmon {
    NONE("none"),
    PIXELMON_REFORGED("com.pixelmonmod.pixelmon.Pixelmon");

    private final String pack;

    EnumPixelmon(String pack) {
        this.pack = pack;
    }

    public String getPackage() {
        return pack;
    }
}
