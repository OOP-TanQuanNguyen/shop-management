package edu.ptithcm.app;

import java.util.HashMap;
import java.util.Map;

public class AppState {

    private final Map<String, Object> data = new HashMap<>();

    public synchronized void set(String key, Object value) {
        data.put(key, value);
    }

    public synchronized Object get(String key) {
        return data.get(key);
    }

    public synchronized Map<String, Object> getAll() {
        return new HashMap<>(data);
    }

    public synchronized void clear() {
        data.clear();
    }

    public synchronized void remove(String key) {
        data.remove(key);
    }
}
