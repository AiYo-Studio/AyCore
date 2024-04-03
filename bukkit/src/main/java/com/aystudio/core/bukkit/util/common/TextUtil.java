package com.aystudio.core.bukkit.util.common;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Blank038
 */
public class TextUtil {
    private static final Pattern PATTERN = Pattern.compile("#[A-f0-9]{6}");

    public static String formatHexColor(String message) {
        String copy = message;
        Matcher matcher = PATTERN.matcher(copy);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            copy = copy.replace(color, String.valueOf(colorOf(color)));
        }
        return ChatColor.translateAlternateColorCodes('&', copy);
    }

    private static String colorOf(String color) {
        try {
            return ChatColor.of(color).toString();
        } catch (Exception ignore) {
            return color;
        }
    }
}
