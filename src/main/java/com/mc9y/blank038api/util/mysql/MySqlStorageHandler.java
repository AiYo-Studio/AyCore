package com.mc9y.blank038api.util.mysql;

import com.mc9y.blank038api.Blank038API;
import com.mc9y.blank038api.util.mysql.execute.ExecuteModel;
import com.mc9y.blank038api.util.mysql.interfaces.AbstractDataSourceHandlerImpl;
import com.mc9y.blank038api.util.mysql.interfaces.impl.CommonDataSourceHandler;
import com.mc9y.blank038api.util.mysql.interfaces.impl.HikariDataSourceHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
    private final AbstractDataSourceHandlerImpl DATA_SOURCE;
    private String queryTable;
    private boolean reconnection;

    public MySqlStorageHandler(JavaPlugin plugin, String url, String user, String password, String... initUpdateStatement) {
        this.DATA_SOURCE = Blank038API.getBlank038API().hasHikariCP() ?
                new HikariDataSourceHandler(plugin, url, user, password) : new CommonDataSourceHandler(plugin, url, user, password);
        // 执行初始语句
        Arrays.stream(initUpdateStatement).forEach(this::updateStatement);
        // 建立线程定时请求, 避免超时
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (reconnection) {
                this.DATA_SOURCE.connect((statement) -> {
                    ResultSet resultSet = null;
                    try {
                        resultSet = statement.executeQuery();
                    } catch (SQLException throwable) {
                        throwable.printStackTrace();
                    } finally {
                        this.DATA_SOURCE.close(statement, resultSet);
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
        return this.DATA_SOURCE;
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
        this.DATA_SOURCE.connect((statement) -> {
            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, sql);
    }

    public void connect(ExecuteModel executeModel, String sql) {
        this.DATA_SOURCE.connect(executeModel, sql);
    }
}