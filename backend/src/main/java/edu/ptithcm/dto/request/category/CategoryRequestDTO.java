package edu.ptithcm.dto.request.category;

import edu.ptithcm.utils.RequestUtil;
import java.util.Map;

public class CategoryRequestDTO {

    private final String categoryId;
    private final String name;

    public CategoryRequestDTO(Map<String, Object> data) {
        this.categoryId = RequestUtil.toStr(data.get("categoryId"));
        this.name = RequestUtil.toStr(data.get("name"));
    }

    // Getters
    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public boolean validForCreate() {
        return name != null && !name.isEmpty();
    }

    public boolean validForUpdate() {
        if (categoryId == null || categoryId.isBlank()) return false;
        return name != null && !name.isBlank();
    }

    public boolean validForDelete() {
        return categoryId != null && !categoryId.isBlank();
    }
}
