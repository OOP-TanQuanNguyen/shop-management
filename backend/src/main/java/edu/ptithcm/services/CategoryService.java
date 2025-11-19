package edu.ptithcm.services;

import java.util.List;
import java.util.UUID;

import edu.ptithcm.dto.request.category.CategoryRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.dto.response.info_models.CategoryInfo;
import edu.ptithcm.models.CategoryModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.category.CategoryRepository;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;

public class CategoryService {

    private static final CategoryRepository categoryRepo = Repository.category();
    private static final BaseMapper<CategoryModel, CategoryInfo> mapper = MapperFactory.category();

    // ------------------ Lấy tất cả ------------------
    public ResponseDTO<List<CategoryInfo>> getAllCategories() throws RuntimeException {
        return new SuccessResponse<>("Lấy danh sách category thành công", mapper.toDTOList(categoryRepo.findAll()));
    }

    // ------------------ Tạo category ------------------
    public ResponseDTO<CategoryInfo> createCategory(CategoryRequestDTO req) throws RuntimeException {

        if (!req.validForCreate())
            return new InvalidResponse<>("Thiếu tên category");

        // check trùng tên
        if (categoryRepo.existsByName(req.getName()))
            return new InvalidResponse<>("Tên category đã tồn tại");

        CategoryModel category = new CategoryModel(UUID.randomUUID().toString(), req.getName());
        categoryRepo.save(category);

        return new SuccessResponse<>("Tạo category thành công", mapper.toDTO(category));
    }

    // ------------------ Cập nhật category ------------------
    public ResponseDTO<CategoryInfo> updateCategory(CategoryRequestDTO req) throws RuntimeException {

        if (!req.validForUpdate())
            return new InvalidResponse<>("Thiếu ID category");

        CategoryModel temp = new CategoryModel(req.getCategoryId(), req.getName());
        CategoryModel updated = categoryRepo.update(temp);

        if (updated == null)
            return new NotFoundResponse<>("Không tìm thấy category để cập nhật");

        return new SuccessResponse<>("Cập nhật category thành công", mapper.toDTO(updated));
    }

    // ------------------ Xóa category ------------------
    public ResponseDTO<CategoryInfo> deleteCategory(CategoryRequestDTO req) throws RuntimeException {

        if (req.getCategoryId() == null || req.getCategoryId().isBlank())
            return new InvalidResponse<>("Thiếu ID category");

        CategoryModel deleted = categoryRepo.delete(req.getCategoryId());

        if (deleted == null)
            return new NotFoundResponse<>("Không tồn tại category");

        return new SuccessResponse<>("Xóa category thành công", mapper.toDTO(deleted));
    }

    // ------------------ Lấy theo ID ------------------
    public ResponseDTO<CategoryInfo> getCategoriesById(CategoryRequestDTO req) throws RuntimeException {
        CategoryModel category = categoryRepo.findById(req.getCategoryId());
        if (category == null)
            return new NotFoundResponse<>("Không tìm thấy category");

        return new SuccessResponse<>("Lấy category thành công", mapper.toDTO(category));
    }
}
