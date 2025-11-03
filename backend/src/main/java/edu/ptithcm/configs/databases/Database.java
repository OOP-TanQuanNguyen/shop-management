package edu.ptithcm.configs.databases;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private static final Map<String, DatabaseAdapter> registry = new ConcurrentHashMap<>();
    private static String default_type = "MYSQL";

    private Database() {}

    public static void register(String type, DatabaseAdapter adapter) {
        registry.put(type.toUpperCase(), adapter);
        System.out.println("[DB] Registered adapter: " + type.toUpperCase());
    }

    public static DatabaseAdapter getInstance() {
        DatabaseAdapter adapter = registry.get(Database.default_type);
        if (adapter == null) {
            throw new IllegalStateException("[DB] No adapter registered for type: " + Database.default_type);
        }
        return adapter;
    }

    public static void setDefaultType(String type){
        Database.default_type = type.toUpperCase();
    }

    static {
        Database.register("MYSQL",MySQLAdapter.getInstance());
    }

}
