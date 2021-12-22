package com.aystudio.core.bukkit.entity;

import com.aystudio.core.bukkit.model.message.BaseMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Blank038
 * @since 2021-09-01
 */
public class AdvancedPlayer {
    private final UUID PLAYER_UUID;

    public AdvancedPlayer(UUID uuid) {
        this.PLAYER_UUID = uuid;
    }

    public void sendMessage(BaseMessage baseMessage) {
        baseMessage.play(this.getPlayer());
    }

    public Player getPlayer() {
        return Bukkit.getServer().getPlayer(this.PLAYER_UUID);
    }
}