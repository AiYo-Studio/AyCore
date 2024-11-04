package com.aystudio.core.bukkit.util.mysql.interfaces;

import com.aystudio.core.bukkit.interfaces.CustomExecute;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.logging.Level;

/**
 * @author Blank038
 * @since 2021-03-16
 */
public abstract class AbstractDataSourceHandlerImpl implements IDataSourceHandler {
    public static boolean SQL_STATUS = false;

    private final JavaPlugin sourcePlugin;
    private String SQL_URL, SQL_USER, SQL_PASSWORD;
    private Connection connection;
    private boolean debug;

    public AbstractDataSourceHandlerImpl(JavaPlugin plugin) {
        this.sourcePlugin = plugin;
    }

    public void setData(String url, String user, String password) {
        this.SQL_URL = url;
        this.SQL_USER = user;
        this.SQL_PASSWORD = password;
    }

    public void connect(CustomExecute<PreparedStatement> executeModel, String sql) {
        if (this.sourcePlugin == null || !this.sourcePlugin.isEnabled()) {
            return;
        }
        PreparedStatement statement = null;
        try {
            if (debug) {
                this.sourcePlugin.getLogger().info("MySQL 连接是否为空: " + (connection == null));
            }
            if (connection == null || connection.isClosed()) {
                if (debug) {
                    this.sourcePlugin.getLogger().info("已重新从 MySQL 取得 Connection. ");
                }
                this.connection = this.getConnection();
            }
            statement = connection.prepareStatement(sql);
            executeModel.run(statement);
        } catch (SQLException e) {
            SQL_STATUS = false;
            connection = this.getConnection();
            this.sourcePlugin.getLogger().log(Level.WARNING, e, () -> "SQL 执行出错: " + sql);
        } finally {
            this.close(statement, null);
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public String getUrl() {
        return this.SQL_URL;
    }

    public String getUser() {
        return this.SQL_USER;
    }

    public String getPassword() {
        return this.SQL_PASSWORD;
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public void close(Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
