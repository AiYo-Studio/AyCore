package com.mc9y.blank038api.model.message.advancement;

import com.mc9y.blank038api.Blank038API;
import com.mc9y.blank038api.model.message.BaseMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Blank038
 * @since 2021-09-01
 */
public class AdvancementMessage extends BaseMessage {
    private final NamespacedKey ADVANCEMENT_ID;
    private final String TOAST_JSON;

    protected AdvancementMessage(String json) {
        this.ADVANCEMENT_ID = new NamespacedKey(Blank038API.getBlank038API(), UUID.randomUUID().toString());
        this.TOAST_JSON = json;
    }

    @Override
    public void play(Player... players) {
        super.play(players);
        // 执行逻辑
        try {
            Advancement advancement = Bukkit.getServer().getUnsafe().loadAdvancement(this.ADVANCEMENT_ID, this.TOAST_JSON);
            Arrays.stream(players).filter(OfflinePlayer::isOnline).forEach((player) -> {
                AdvancementProgress progress = player.getAdvancementProgress(advancement);
                progress.getRemainingCriteria().forEach(progress::awardCriteria);
                Bukkit.getScheduler().runTaskLaterAsynchronously(Blank038API.getBlank038API(),
                        () -> progress.getAwardedCriteria().forEach(progress::revokeCriteria), 10L);
            });
        } finally {
            if (Bukkit.getAdvancement(ADVANCEMENT_ID) != null) {
                Bukkit.getServer().getUnsafe().removeAdvancement(this.ADVANCEMENT_ID);
            }
        }
    }
}
