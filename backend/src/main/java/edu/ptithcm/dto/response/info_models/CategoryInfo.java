package edu.ptithcm.dto.response.info_models;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

public class CategoryInfo {
    private final String categoryId;
    private final String name;
    private final boolean active;
    private final LocalDateTime createdAt;

    private CategoryInfo(Builder b) {
        this.categoryId = b.categoryId;
        this.name = b.name;
        this.active = b.active;
        this.createdAt = b.createdAt;
    }

    public String getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("name", name);
        map.put("active", active);
        map.put("createdAt", createdAt != null ? createdAt.toString() : null);
        return map;
    }

    public static class Builder {
        private String categoryId;
        private String name;
        private boolean active = true;
        private LocalDateTime createdAt;

        public Builder categoryId(String categoryId) { this.categoryId = categoryId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CategoryInfo build() { return new CategoryInfo(this); }
    }
}
