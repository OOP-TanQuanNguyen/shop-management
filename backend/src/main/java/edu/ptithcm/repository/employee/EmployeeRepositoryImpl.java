package edu.ptithcm.repository.employee;

import edu.ptithcm.models.EmployeeModel;
import edu.ptithcm.repository.BaseRepository;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Map;

/**
 * Implementation of EmployeeRepository using Hibernate ORM.
 */
public class EmployeeRepositoryImpl extends BaseRepository<EmployeeModel> implements EmployeeRepository {

    @Override
    public void save(EmployeeModel entity) {
        execute(session -> {
            session.persist(entity);
            return null;
        });
    }

    @Override
    public void update(EmployeeModel entity) {
        execute(session -> {
            session.merge(entity);
            return null;
        });
    }

    @Override
    public void delete(EmployeeModel entity) {
        execute(session -> {
            EmployeeModel managed = session.get(EmployeeModel.class, entity.getId());
            if (managed != null) {
                session.remove(managed); // giờ thuộc session hiện tại
            }
            return null;
        });
    }


    @Override
    public EmployeeModel findById(String id) {
        return execute(session -> session.get(EmployeeModel.class, id));
    }

    @Override
    public List<EmployeeModel> findAll() {
        return execute(session ->
            session.createQuery("FROM EmployeeModel e ORDER BY e.startAt DESC", EmployeeModel.class).list());
    }

    @Override
    public boolean existsByUsername(String username) {
        return execute(session -> {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(e) FROM EmployeeModel e WHERE e.username = :username", Long.class);
            query.setParameter("username", username);
            return query.uniqueResult() > 0;
        });
    }

    @Override
    public EmployeeModel findByUsername(String username) {
        return execute(session -> {
            Query<EmployeeModel> query = session.createQuery(
                "FROM EmployeeModel e WHERE e.username = :username", EmployeeModel.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        });
    }

    @Override
    public List<EmployeeModel> findActive() {
        return execute(session ->
            session.createQuery(
                "FROM EmployeeModel e WHERE e.status = true ORDER BY e.startAt DESC", EmployeeModel.class)
            .list());
    }

    @Override
    public List<EmployeeModel> filter(Map<String, Object> filters) {
        return execute(session -> {
            StringBuilder hql = new StringBuilder("FROM EmployeeModel e WHERE 1=1 ");
            if (filters.containsKey("branchId")) hql.append("AND e.branch.id = :branchId ");
            if (filters.containsKey("role")) hql.append("AND e.role = :role ");
            if (filters.containsKey("status")) hql.append("AND e.status = :status ");

            Query<EmployeeModel> query = session.createQuery(hql.toString(), EmployeeModel.class);

            if (filters.containsKey("branchId")) query.setParameter("branchId", filters.get("branchId"));
            if (filters.containsKey("role")) query.setParameter("role", filters.get("role"));
            if (filters.containsKey("status")) query.setParameter("status", filters.get("status"));

            return query.list();
        });
    }
}
