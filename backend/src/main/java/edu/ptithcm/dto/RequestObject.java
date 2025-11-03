package edu.ptithcm.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestObject {
    private final Map<String, Object> data;

    public RequestObject() {
        this.data = new HashMap<>();
    }

    public RequestObject(Map<String, Object> data) {
        this.data = data != null ? new HashMap<>(data) : new HashMap<>();
    }

    public Object get(String key) {
        return data.get(key);
    }

    public String getString(String key, String defaultValue) {
        Object val = data.get(key);
        return val != null ? val.toString() : defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        Object val = data.get(key);
        if (val instanceof Number) return ((Number) val).intValue();
        try { return Integer.parseInt(val.toString()); } catch (Exception e) { return defaultValue; }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object val = data.get(key);
        if (val instanceof Boolean) return (Boolean) val;
        try { return Boolean.parseBoolean(val.toString()); } catch (Exception e) { return defaultValue; }
    }

    public Map<String, Object> getFilters() {
        Object val = data.get("filters");
        if (val instanceof Map) {
            return (Map<String, Object>) val;
        }
        return Collections.emptyMap();
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(data);
    }
    public Map<String,Object> getMap(String key) {
        Object obj = data.get(key);
        if(obj instanceof Map<?,?>) {
            return (Map<String,Object>) obj;
        }
        return null;
    }
}
