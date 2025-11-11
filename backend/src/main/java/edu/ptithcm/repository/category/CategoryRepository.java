package edu.ptithcm.repository.category;

import java.sql.SQLException;
import java.util.List;

import edu.ptithcm.model.CategoryModel;

public interface CategoryRepository {
    boolean exists(String name) throws SQLException;
    void create(CategoryModel category) throws SQLException;
    List<CategoryModel> getAll() throws SQLException;
    void update(String id, String name) throws SQLException;
    void remove(String id) throws SQLException;
    CategoryModel findById(String id) throws SQLException;
}
