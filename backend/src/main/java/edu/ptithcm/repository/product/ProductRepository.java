package edu.ptithcm.repository.product;

import java.util.List;

import edu.ptithcm.models.ProductModel;
import edu.ptithcm.repository.GenericRepository;

public interface ProductRepository extends GenericRepository<ProductModel, String> {
    List<ProductModel> searchByName(String keyword);
}
