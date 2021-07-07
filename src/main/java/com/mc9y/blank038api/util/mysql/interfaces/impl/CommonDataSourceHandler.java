package com.mc9y.blank038api.util.mysql.interfaces.impl;

import com.mc9y.blank038api.util.mysql.interfaces.AbstractDataSourceHandlerImpl;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Blank038
 * @since 2021-03-11
 */
public class CommonDataSourceHandler extends AbstractDataSourceHandlerImpl {

    public CommonDataSourceHandler(JavaPlugin plugin, String url, String user, String password) {
        super(plugin);
        this.setData(url, user, password);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(this.getUrl(), this.getUser(), this.getPassword());
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}