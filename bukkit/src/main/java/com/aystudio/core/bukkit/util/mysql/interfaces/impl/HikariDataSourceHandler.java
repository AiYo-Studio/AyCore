package com.aystudio.core.bukkit.util.mysql.interfaces.impl;

import com.aystudio.core.bukkit.util.mysql.interfaces.AbstractDataSourceHandlerImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Blank038
 * @since 2021-03-11
 */
public class HikariDataSourceHandler extends AbstractDataSourceHandlerImpl {
    private final HikariDataSource DATA_SOURCE;

    public HikariDataSourceHandler(JavaPlugin plugin, String url, String user, String password) {
        super(plugin);
        this.setData(url, user, password);
        // 初始化 HikariCP 源
        HikariConfig config = new HikariConfig();
        config.setPoolName(plugin.getDescription().getName() + "-Pool");
        config.setJdbcUrl(this.getUrl());
        config.setUsername(this.getUser());
        config.setPassword(this.getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("autoReconnect", "true");
        config.addDataSourceProperty("useSSL", "false");
        config.addDataSourceProperty("maxPoolSize", "3");
        this.DATA_SOURCE = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() {
        try {
            return this.DATA_SOURCE.getConnection();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

}
