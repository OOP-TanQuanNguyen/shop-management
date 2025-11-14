package edu.ptithcm.repository.product;

import java.util.List;

import org.hibernate.query.Query;

import edu.ptithcm.models.ProductModel;
import edu.ptithcm.repository.BaseRepository;


public class ProductRepositoryImpl extends BaseRepository<ProductModel> implements ProductRepository {

    // -------------------- SAVE --------------------
    @Override
    public void save(ProductModel entity) {
        execute(session -> {
            session.persist(entity);
            return null;
        });
    }

    @Override
    public ProductModel update(ProductModel newData) {
        return execute(session -> {
            ProductModel managed = session.get(ProductModel.class, newData.getId());
            if (managed == null) return null;

            // Dirty-check: chỉ set khi có thay đổi
            if (newData.getName() != null) managed.setName(newData.getName());
            if (newData.getCategory() != null) managed.setCategory(newData.getCategory());
            if (newData.getCostPrice() != null && newData.getCostPrice() >= 0)
                managed.setCostPrice(newData.getCostPrice());
            if (newData.getSellPrice() != null && newData.getSellPrice() >= 0)
                managed.setSellPrice(newData.getSellPrice());
            if (newData.getExpiryDate() != null) managed.setExpiryDate(newData.getExpiryDate());
            managed.setActive(newData.isActive());

            return managed;
        });
    }

    @Override
    public ProductModel delete(String id) {
        return execute(session -> {
            ProductModel managed = session.get(ProductModel.class, id);
            if (managed == null) return null;
            session.remove(managed);
            return managed;
        });
    }

    @Override
    public ProductModel findById(String id) {
        return execute(session -> session.get(ProductModel.class, id));
    }

    @Override
    public List<ProductModel> findAll() {
        return execute(session -> session.createQuery(
            "FROM ProductModel p ORDER BY p.createdAt DESC", ProductModel.class
        ).list());
    }

    @Override
    public List<ProductModel> searchByName(String keyword) {
        return execute(session -> {
            Query<ProductModel> query = session.createQuery(
                "FROM ProductModel p " +
                "WHERE LOWER(p.name) LIKE LOWER(:kw) " +
                "ORDER BY p.name ASC",
                ProductModel.class
            );
            query.setParameter("kw", "%" + keyword + "%");
            query.setMaxResults(50);
            return query.list();
        });
    }
}
