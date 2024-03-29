package com.aystudio.core.bukkit.util.mysql;

import com.aystudio.core.bukkit.AyCore;
import com.aystudio.core.bukkit.helper.SchedulerHelper;
import com.aystudio.core.bukkit.interfaces.CustomExecute;
import com.aystudio.core.bukkit.util.mysql.interfaces.AbstractDataSourceHandlerImpl;
import com.aystudio.core.bukkit.util.mysql.interfaces.impl.CommonDataSourceHandler;
import com.aystudio.core.bukkit.util.mysql.interfaces.impl.HikariDataSourceHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * MySQL 数据处理类
 *
 * @author Blank038
 * @since 2021-03-16
 */
public class MySqlStorageHandler {
    private AbstractDataSourceHandlerImpl dataSource;
    private String queryTable;
    private boolean reconnection;

    public MySqlStorageHandler(JavaPlugin plugin, String url, String user, String password, String... initUpdateStatement) {
        try {
            this.dataSource = AyCore.getInstance().hasHikariCP() && AyCore.getInstance().isSameHikariVersion()
                    && AyCore.getInstance().getConfig().getBoolean("hikari", true)
                    ? new HikariDataSourceHandler(plugin, url, user, password) : new CommonDataSourceHandler(plugin, url, user, password);
        } catch (Exception ignored) {
            this.dataSource = new CommonDataSourceHandler(plugin, url, user, password);
        }
        // 执行初始语句
        Arrays.stream(initUpdateStatement).forEach(this::updateStatement);
        // 建立线程定时请求, 避免超时
        SchedulerHelper.runTaskTimerAsync(plugin, () -> {
            if (reconnection) {
                this.dataSource.connect((statement) -> {
                    ResultSet resultSet = null;
                    try {
                        resultSet = statement.executeQuery();
                    } catch (SQLException throwable) {
                        throwable.printStackTrace();
                    } finally {
                        this.dataSource.close(statement, resultSet);
                    }
                }, "show full columns from " + queryTable);
            }
        }, 1200L, 1200L);
    }

    /**
     * 获取 MySQL 连接源接口对象
     *
     * @return 连接源对象
     */
    public AbstractDataSourceHandlerImpl getDataSource() {
        return this.dataSource;
    }

    /**
     * 获取 MySQL 数据连接源是否自动重连
     *
     * @return 是否重连
     */
    public boolean isReconnection() {
        return this.reconnection;
    }

    /**
     * 设置是否自动重连
     *
     * @param reconnection 是否自动重连
     */
    public void setCheckConnection(boolean reconnection) {
        this.reconnection = reconnection;
    }

    public String getReconnectionQueryTable() {
        return this.queryTable;
    }

    /**
     * 设置重新连接时目标表
     *
     * @param table 目标表名
     */
    public void setReconnectionQueryTable(String table) {
        this.queryTable = table;
    }

    public void updateStatement(String sql) {
        this.dataSource.connect((statement) -> {
            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, sql);
    }

    public void connect(CustomExecute<PreparedStatement> executeModel, String sql) {
        this.dataSource.connect(executeModel, sql);
    }
}