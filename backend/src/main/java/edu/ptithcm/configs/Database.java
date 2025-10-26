package edu.ptithcm.configs;
import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {
    private static Database instance;
    private final HikariDataSource connectionPool;

    private Database() {
        HikariConfig config = new HikariConfig();
        //connect DB
        config.setJdbcUrl(Config.DatabaseConfig.DB_URL);
        config.setUsername(Config.DatabaseConfig.DB_USERNAME);
        config.setPassword(Config.DatabaseConfig.DB_PASSWORD);
        //cấu hình pool
        config.setMaximumPoolSize(10); // số kết nối tối đa trong pool
        config.setMinimumIdle(2); // số kết nối tối thiểu giữ trong pool
        config.setIdleTimeout(60000); // thời gian chờ trước khi đóng kết nối không sử dụng
        config.setConnectionTimeout(30000); // thời gian chờ để lấy kết nối từ pool
        config.setMaxLifetime(1800000);// thời gian sống tối đa của một kết nối
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // tạo pool nà
        this.connectionPool = new HikariDataSource(config);
        System.out.println("[DB] HikariCP pool initialized");
    }
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }   
    public Connection getConnection() {
        try{
            Connection conn = this.connectionPool.getConnection();
            System.out.println("[DB] Connection acquired from pool");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
