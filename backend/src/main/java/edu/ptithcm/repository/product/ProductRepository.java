package edu.ptithcm.repository.product;

import edu.ptithcm.models.ProductModel;
import edu.ptithcm.repository.GenericRepository;
import java.util.List;

public interface ProductRepository extends GenericRepository<ProductModel, String> {
    List<ProductModel> searchByName(String keyword);
}
