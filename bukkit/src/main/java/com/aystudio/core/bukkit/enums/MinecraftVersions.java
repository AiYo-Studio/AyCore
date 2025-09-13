package com.aystudio.core.bukkit.enums;

import com.aystudio.core.common.data.CommonData;
import org.bukkit.Bukkit;

public enum MinecraftVersions {
    Unknown,
    v1_6_R3,
    v1_7_R1,
    v1_7_R2,
    v1_7_R3,
    v1_7_R4,
    v1_8_R1,
    v1_8_R2,
    v1_8_R3,
    v1_9_R1,
    v1_9_R2,
    v1_10_R1,
    v1_11_R1,
    v1_12_R1,
    v1_13_R1,
    v1_13_R2,
    v1_14_R1,
    v1_15_R1,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3,
    v1_17_R1,
    v1_18_R1,
    v1_18_R2,
    v1_19_R1,
    v1_19_R2,
    v1_19_R3,
    v1_20_R1,
    v1_20_R2,
    v1_20_R3,
    v1_20_R4,
    v1_21_R1;

    public static boolean isBeforeVersion(MinecraftVersions version) {
        return isBeforeVersion(version, true);
    }

    public static boolean isBeforeVersion(MinecraftVersions version, boolean contain) {
        MinecraftVersions current = getVersion();
        if (current == Unknown || version == Unknown) {
            return false;
        }
        return contain ? current.ordinal() <= version.ordinal() : current.ordinal() < version.ordinal();
    }

    public static boolean isAfterVersion(MinecraftVersions version) {
        return isAfterVersion(version, true);
    }

    public static boolean isAfterVersion(MinecraftVersions version, boolean contain) {
        MinecraftVersions current = getVersion();
        if (current == Unknown || version == Unknown) {
            return false;
        }
        return contain ? current.ordinal() >= version.ordinal() : current.ordinal() > version.ordinal();
    }

    public static MinecraftVersions getVersion() {
        if (CommonData.currentVersion != null) {
            return CommonData.currentVersion;
        }
        try {
            String[] parts = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            if (parts.length > 3) {
                return MinecraftVersions.valueOf(parts[3]);
            }
            MinecraftVersions version = MinecraftVersions.Unknown;
            switch (Bukkit.getVersion().split("-")[0]) {
                case "1.17":
                case "1.17.1":
                    version = MinecraftVersions.v1_17_R1;
                    break;
                case "1.18":
                case "1.18.1":
                    version = MinecraftVersions.v1_18_R1;
                    break;
                case "1.19":
                case "1.19.1":
                case "1.19.2":
                    version = MinecraftVersions.v1_19_R1;
                    break;
                case "1.19.3":
                    version = MinecraftVersions.v1_19_R2;
                    break;
                case "1.19.4":
                    version = MinecraftVersions.v1_19_R3;
                    break;
                case "1.20":
                case "1.20.1":
                    version = MinecraftVersions.v1_20_R1;
                    break;
                case "1.20.2":
                    version = MinecraftVersions.v1_20_R2;
                    break;
                case "1.20.3":
                case "1.20.4":
                    version = MinecraftVersions.v1_20_R3;
                    break;
                case "1.20.5":
                case "1.20.6":
                    version = MinecraftVersions.v1_20_R4;
                    break;
                case "1.21":
                case "1.21.1":
                    version = MinecraftVersions.v1_21_R1;
                    break;
            }
            return version;
        } catch (Exception ignored) {
        }
        return MinecraftVersions.Unknown;
    }
}
