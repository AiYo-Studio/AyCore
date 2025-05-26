package com.aystudio.core.bukkit.util.mysql.litesql.wrapper;

import com.aystudio.core.bukkit.thread.BlankThread;
import com.aystudio.core.bukkit.thread.ThreadProcessor;
import com.aystudio.core.bukkit.util.mysql.litesql.dao.BasisDAO;
import com.aystudio.core.bukkit.util.mysql.litesql.entity.IEntity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@Setter
public class LiteWrapper<T extends IEntity> implements Listener {
    private final JavaPlugin plugin;
    private final BasisDAO<T> dao;
    private boolean pullNotify = false;
    private String loadingMessage = "", successMessage = "";
    private int timeout = 120;
    private Function<Player, T> saveFunc;
    private BiConsumer<Player, T> loadedFunc;

    public LiteWrapper(JavaPlugin plugin, Class<T> entityClass, String url, String user, String password, String table) {
        this.plugin = plugin;
        this.dao = new BasisDAO<>(plugin, entityClass, url, user, password, table);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    protected void onPlayerJoin(PlayerJoinEvent event) {
        if (pullNotify) {
            event.getPlayer().sendMessage(loadingMessage);
        }
        ThreadProcessor.crateTask(plugin, new BlankThread(10) {
            private int count;

            @Override
            public void run() {
                Player player = event.getPlayer();
                if (player == null || !player.isOnline()) {
                    this.cancel();
                    return;
                }
                if (!dao.isLocked(player)) {
                    loadData(player);
                    this.cancel();
                } else {
                    count++;
                    if (count > timeout) {
                        loadData(player);
                        this.cancel();
                    }
                }
            }
        });
    }

    @EventHandler
    protected void onPlayerQuit(PlayerQuitEvent event) {
        if (saveFunc != null) {
            T t = saveFunc.apply(event.getPlayer());
            if (t == null) {
                return;
            }
            this.dao.save(t, 1);
        }
    }

    private void loadData(Player player) {
        dao.setLocked(player, true);
        if (loadedFunc != null) {
            T cache = dao.get(player.getName());
            loadedFunc.accept(player, cache);
        }
        if (pullNotify) {
            player.sendMessage(successMessage);
        }
    }
}
