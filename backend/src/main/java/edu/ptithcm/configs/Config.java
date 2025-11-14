package edu.ptithcm.configs;

public class Config{

    private Config(){}

    public static class AppConfig{
        private AppConfig() {}
        public static final int SERVER_PORT = 2025;
        public static final String SERVER_HOST = "localhost";
    }
}
