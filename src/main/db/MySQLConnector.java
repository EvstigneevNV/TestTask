package db;

import settings.Settings;

import java.sql.*;
import java.util.Map;

public class MySQLConnector implements IDBConnector {
    private static Connection connection = null;
    private static Statement statement = null;

    public MySQLConnector() {
        this.open();
    }

    private static void open(){
        Settings confReader = new Settings();
        Map<String, String> confData = confReader.getSettings();
        try {
            if (connection == null) {
                connection = DriverManager.getConnection(String.format("%s/%s", confData.get("url"), confData.get("name")), confData.get("username"), confData.get("password"));
            }
            if (statement == null) {
                statement= connection.createStatement();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void close() {
        if (statement != null) {
            try {
                statement.close();
                statement = null;
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void  execute(String sqlRequest) {
        try {
            statement.execute(sqlRequest);
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ResultSet executeQuery(String sqlRequest) {
        try {
            return statement.executeQuery(sqlRequest);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}