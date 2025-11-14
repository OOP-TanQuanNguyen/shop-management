package edu.ptithcm.repository.category;

import java.util.List;

import org.hibernate.query.Query;

import edu.ptithcm.models.CategoryModel;
import edu.ptithcm.repository.BaseRepository;

public class CategoryRepositoryImpl extends BaseRepository<CategoryModel> implements CategoryRepository {

    @Override
    public void save(CategoryModel entity) {
        execute(session -> {
            session.persist(entity);
            return null;
        });
    }

    @Override
    public CategoryModel update(CategoryModel newData) {
        return execute(session -> {
            CategoryModel managed = session.get(CategoryModel.class, newData.getId());
            if (managed == null) return null;
            // Dirty checking — Hibernate tự flush khi commit
            if (newData.getName() != null) managed.setName(newData.getName());

            return managed;
        });
    }

    @Override
    public CategoryModel delete(String id) {
        return execute(session -> {
            CategoryModel managed = session.get(CategoryModel.class, id);
            if (managed == null) return null;
            session.remove(managed);
            return managed;
        });
    }

    @Override
    public CategoryModel findById(String id) {
        return execute(session -> session.get(CategoryModel.class, id));
    }

    @Override
    public List<CategoryModel> findAll() {
        return execute(session ->
            session.createQuery("FROM CategoryModel c ORDER BY c.createdAt DESC", CategoryModel.class).list()
        );
    }

    @Override
    public boolean existsByName(String name) {
        return execute(session -> {
            Query<Integer> q = session.createQuery(
                "SELECT 1 FROM CategoryModel c WHERE c.name = :name", Integer.class);
            q.setParameter("name", name);
            q.setMaxResults(1);
            return q.uniqueResult() != null;
        });
    }

    @Override
    public List<CategoryModel> findAllOrdered() {
        return execute(session ->
            session.createQuery(
                "FROM CategoryModel c WHERE c.active = true ORDER BY c.name ASC",
                CategoryModel.class
            ).list()
        );
    }
}
