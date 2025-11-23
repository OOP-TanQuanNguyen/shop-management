package edu.ptithcm.models;

import java.util.Map;

public class CategoryInfo {

    private String categoryId;
    private String name;

    public CategoryInfo() {
    }

    // Constructor nhận Map từ BE
    public CategoryInfo(Map<String, Object> data) {
        this.categoryId = (String) data.get("categoryId");
        this.name = (String) data.get("name");
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public void setCategoryId(String id) {
        this.categoryId = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
