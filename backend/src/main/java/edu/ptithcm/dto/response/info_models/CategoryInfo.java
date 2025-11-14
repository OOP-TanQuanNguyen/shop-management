package edu.ptithcm.dto.response.info_models;

import java.util.HashMap;
import java.util.Map;

public class CategoryInfo {
    private final String categoryId;
    private final String name;

    private CategoryInfo(Builder b) {
        this.categoryId = b.categoryId;
        this.name = b.name;
    }

    public String getCategoryId() { return categoryId; }
    public String getName() { return name; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("name", name);
        return map;
    }

    public static class Builder {
        private String categoryId;
        private String name;

        public Builder categoryId(String categoryId) { this.categoryId = categoryId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public CategoryInfo build() { return new CategoryInfo(this); }
    }
}
