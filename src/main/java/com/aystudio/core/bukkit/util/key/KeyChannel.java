package com.aystudio.core.bukkit.util.key;

import com.aystudio.core.bukkit.AyCore;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

/**
 * @author Blank038
 */
public class KeyChannel {

    public void sendMessage(Player player, String message) {
        if (player != null && player.isOnline()) {
            player.sendPluginMessage(AyCore.getBlank038API(), "keyexecute", message.getBytes(StandardCharsets.UTF_8));
        }
    }
}