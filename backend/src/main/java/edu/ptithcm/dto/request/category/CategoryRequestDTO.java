package edu.ptithcm.dto.request.category;

import java.util.Map;

public class CategoryRequestDTO {

    private final String categoryId;
    private final String name;

    public CategoryRequestDTO(Map<String, Object> data) {
        this.categoryId = (String) data.get("categoryId");
        this.name = (String) data.get("name");
    }

    // Getters
    public String getCategoryId() { return categoryId; }
    public String getName() { return name; }

    // Validate trước khi tạo
    public boolean validForCreate() {
        return name != null && !name.isEmpty();
    }

    // Validate trước khi update
    public boolean validForUpdate() {
        return categoryId != null && !categoryId.isEmpty() && validForCreate();
    }
}
