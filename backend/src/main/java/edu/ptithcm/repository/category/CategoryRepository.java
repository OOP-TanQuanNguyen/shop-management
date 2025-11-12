package edu.ptithcm.repository.category;

import java.util.List;

import edu.ptithcm.models.CategoryModel;
import edu.ptithcm.repository.GenericRepository;

public interface CategoryRepository extends GenericRepository<CategoryModel, String> {
    boolean existsByName(String name);
    List<CategoryModel> findAllOrdered();
}
