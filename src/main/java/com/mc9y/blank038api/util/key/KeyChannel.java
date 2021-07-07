package com.mc9y.blank038api.util.key;

import com.mc9y.blank038api.Blank038API;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

/**
 * @author Blank038
 */
public class KeyChannel {

    public void sendMessage(Player player, String message) {
        if (player != null && player.isOnline()) {
            player.sendPluginMessage(Blank038API.getBlank038API(), "keyexecute", message.getBytes(StandardCharsets.UTF_8));
        }
    }
}