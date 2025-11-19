package edu.ptithcm.controller;

import java.util.List;

import edu.ptithcm.dto.request.category.CategoryRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.CategoryInfo;
import edu.ptithcm.services.CategoryService;
import edu.ptithcm.utils.handle_exception.SafeExecutor;

public class CategoryController {

    private static final CategoryService service = new CategoryService();

    public ResponseDTO<CategoryInfo> createCategory(CategoryRequestDTO req) {
        return SafeExecutor.run(() -> service.createCategory(req));
    }

    public ResponseDTO<CategoryInfo> updateCategory(CategoryRequestDTO req) {
        return SafeExecutor.run(() -> service.updateCategory(req));
    }

    public ResponseDTO<CategoryInfo> deleteCategory(CategoryRequestDTO req) {
        return SafeExecutor.run(() -> service.deleteCategory(req));
    }

    public ResponseDTO<List<CategoryInfo>> getAllCategories() {
        return SafeExecutor.run(() -> service.getAllCategories());
    }

    public ResponseDTO<CategoryInfo> getCategoriesById(CategoryRequestDTO req) {
        return SafeExecutor.run(() -> service.getCategoriesById(req));
    }
}
