package edu.ptithcm.configs;

public class Config{

    public Config(){}

    public static class DatabaseConfig{
        public static final String DB_URL = "jdbc:mysql://mini_market_db:3306/mini_market?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        public static final String DB_USERNAME = "shop_user";
        public static final String DB_PASSWORD = "pass123";
    }

    public static class AppConfig{
        public static final int SERVER_PORT = 2025;
        public static final String SERVER_HOST = "localhost";
    }
}
