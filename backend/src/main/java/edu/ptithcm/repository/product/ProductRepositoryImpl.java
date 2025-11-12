package edu.ptithcm.repository.product;

import edu.ptithcm.models.ProductModel;
import edu.ptithcm.repository.BaseRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class ProductRepositoryImpl extends BaseRepository<ProductModel> implements ProductRepository {

    @Override
    public void save(ProductModel entity) {
        execute(session -> { session.persist(entity); return null; });
    }

    @Override
    public void update(ProductModel entity) {
        execute(session -> { session.merge(entity); return null; });
    }

    @Override
    public void delete(ProductModel entity) {
        execute(session -> { session.remove(entity); return null; });
    }

    @Override
    public ProductModel findById(String id) {
        return execute(session -> session.get(ProductModel.class, id));
    }

    @Override
    public List<ProductModel> findAll() {
        return execute(session ->
            session.createQuery("FROM ProductModel ORDER BY createdAt DESC", ProductModel.class)
                   .list()
        );
    }

    @Override
    public List<ProductModel> searchByName(String keyword) {
        return execute(session -> {
            Query<ProductModel> q = session.createQuery(
                "FROM ProductModel p WHERE lower(p.name) LIKE :kw ORDER BY p.name",
                ProductModel.class
            );
            q.setParameter("kw", "%" + keyword.toLowerCase() + "%");
            return q.list();
        });
    }
}
