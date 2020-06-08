package Entities.dao;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteManager {
    private static final String DB_URL = "jdbc:sqlite:companies_data.sqlite";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            connection = DriverManager.getConnection(DB_URL, config.toProperties());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return connection;
    }
}
