package edu.ptithcm.configs.databases;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import edu.ptithcm.configs.Config;

public class MySQLAdapter implements DatabaseAdapter {

    private static MySQLAdapter instance;
    private HikariDataSource dataSource;

    private MySQLAdapter() {}

    @Override
    public void init() {
        if (dataSource != null) {
            System.out.println("[DB] MySQLAdapter đã được khởi tạo rồi.");
            return;
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Config.MySQLDatabaseConfig.DB_URL);
        config.setUsername(Config.MySQLDatabaseConfig.DB_USERNAME);
        config.setPassword(Config.MySQLDatabaseConfig.DB_PASSWORD);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // cấu hình pool
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(60000);
        config.setConnectionTimeout(30000);
        config.setMaxLifetime(1800000);

        dataSource = new HikariDataSource(config);
        System.out.println("[DB]  MySQLAdapter initialized (HikariCP ready)");
    }


    public static synchronized MySQLAdapter getInstance() {
        if (instance == null) {
            instance = new MySQLAdapter();
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        try {
            Connection conn = dataSource.getConnection();
            System.out.println("[DB] Connection acquired from MySQL pool");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void close() {
        if (dataSource != null) {
            dataSource.close();
            System.out.println("[DB]  MySQLAdapter pool closed");
        }
    }

}
