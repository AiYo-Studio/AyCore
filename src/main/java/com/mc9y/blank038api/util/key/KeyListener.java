package com.mc9y.blank038api.util.key;

import com.google.gson.JsonObject;
import com.mc9y.blank038api.Blank038API;
import com.mc9y.blank038api.event.KeyEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * @author Blank038
 */
public class KeyListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] data) {
        if ("keyexecute".equals(channel)) {
            String message = new String(data);
            if (message.length() > 64) {
                return;
            }
            try {
                JsonObject jsonObject = Blank038API.GSON.fromJson(message, JsonObject.class);
                if (jsonObject.has("key") && jsonObject.has("down")) {
                    KeyEvent keyEvent = new KeyEvent(player, jsonObject.get("key").getAsInt(), jsonObject.get("down").getAsBoolean());
                    Bukkit.getPluginManager().callEvent(keyEvent);
                }
            } catch (Exception ignored) {
            }
        }
    }
}