package com.aystudio.core.bukkit.util.mysql.litesql;

import com.aystudio.core.bukkit.util.mysql.litesql.entity.IEntity;
import com.aystudio.core.bukkit.util.mysql.litesql.wrapper.LiteWrapper;
import org.bukkit.plugin.java.JavaPlugin;

public class LiteSQLApi {

    public static <T extends IEntity> LiteWrapper<T> create(JavaPlugin plugin, Class<T> entityClass, String url, String user, String password, String table) {
        return new LiteWrapper<>(plugin, entityClass, url, user, password, table);
    }
}
