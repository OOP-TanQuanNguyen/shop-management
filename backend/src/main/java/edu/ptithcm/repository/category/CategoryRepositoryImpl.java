package edu.ptithcm.repository.category;

import edu.ptithcm.models.CategoryModel;
import edu.ptithcm.repository.BaseRepository;
import org.hibernate.query.Query;
import java.util.List;

public class CategoryRepositoryImpl extends BaseRepository<CategoryModel> implements CategoryRepository {

    @Override
    public void save(CategoryModel entity) {
        execute(session -> {
            session.persist(entity);
            return null;
        });
    }

    @Override
    public void update(CategoryModel entity) {
        execute(session -> {
            session.merge(entity);
            return null;
        });
    }

    @Override
    public void delete(CategoryModel entity) {
        execute(session -> {
            CategoryModel managed = session.get(CategoryModel.class, entity.getId());
            if (managed != null) {
                session.remove(managed); // giờ thuộc session hiện tại
            }
            return null;
        });
    }


    @Override
    public CategoryModel findById(String id) {
        return execute(session -> session.get(CategoryModel.class, id));
    }

    @Override
    public List<CategoryModel> findAll() {
        return execute(session ->
            session.createQuery("FROM CategoryModel c", CategoryModel.class).list()
        );
    }

    @Override
    public List<CategoryModel> findAllOrdered() {
        return execute(session ->
            session.createQuery("FROM CategoryModel c ORDER BY c.name ASC", CategoryModel.class).list()
        );
    }

    @Override
    public boolean existsByName(String name) {
        return execute(session -> {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(c) FROM CategoryModel c WHERE LOWER(c.name) = :name",
                Long.class
            );
            query.setParameter("name", name.toLowerCase());
            return query.uniqueResult() > 0;
        });
    }
}
