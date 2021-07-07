package com.mc9y.blank038api.util.key;

import com.mc9y.blank038api.event.KeyEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.json.JSONObject;

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
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.has("key") && jsonObject.has("down")) {
                    KeyEvent keyEvent = new KeyEvent(player, jsonObject.getInt("key"), jsonObject.getBoolean("down"));
                    Bukkit.getPluginManager().callEvent(keyEvent);
                }
            } catch (Exception ignored) {
            }
        }
    }
}