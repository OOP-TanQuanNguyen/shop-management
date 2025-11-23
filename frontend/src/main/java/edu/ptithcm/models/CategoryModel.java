package edu.ptithcm.models;

import java.util.Map;

public class CategoryModel {

    private String categoryId;
    private String name;

    private CategoryModel(Builder builder) {
        this.categoryId = builder.categoryId;
        this.name = builder.name;
    }

    // Getters
    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    // Static factory method from Map
    public static CategoryModel fromMap(Map<String, Object> data) {
        return new Builder()
                .categoryId((String) data.get("categoryId"))
                .name((String) data.get("name"))
                .build();
    }

    // Builder Pattern
    public static class Builder {

        private String categoryId;
        private String name;

        public Builder categoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public CategoryModel build() {
            return new CategoryModel(this);
        }
    }

    @Override
    public String toString() {
        return "CategoryModel{"
                + "categoryId='" + categoryId + '\''
                + ", name='" + name + '\''
                + '}';
    }
}
