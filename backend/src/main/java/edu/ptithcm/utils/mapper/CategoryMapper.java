package edu.ptithcm.utils.mapper;

import edu.ptithcm.dto.response.info_models.CategoryInfo;
import edu.ptithcm.models.CategoryModel;

public class CategoryMapper implements BaseMapper<CategoryModel, CategoryInfo> {

    @Override
    public CategoryInfo toDTO(CategoryModel entity) {
        if (entity == null) return null;
        return new CategoryInfo.Builder()
                .categoryId(entity.getId())
                .name(entity.getName())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
