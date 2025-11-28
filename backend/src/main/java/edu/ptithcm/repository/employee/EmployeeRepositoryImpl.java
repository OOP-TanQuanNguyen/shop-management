package edu.ptithcm.repository.employee;

import java.util.List;
import java.util.Map;

import org.hibernate.query.Query;

import edu.ptithcm.models.EmployeeModel;
import edu.ptithcm.repository.BaseRepository;

public class EmployeeRepositoryImpl extends BaseRepository<EmployeeModel> implements EmployeeRepository {

    @Override
    public void save(EmployeeModel entity) {
        execute(session -> {
            session.persist(entity);
            return null;
        });
    }

    @Override
    public EmployeeModel update(EmployeeModel newData) {
        return execute(session -> {
            EmployeeModel managed = session.get(EmployeeModel.class, newData.getId());
            if (managed == null) {
                return null;
            }

            if (newData.getName() != null) {
                managed.setName(newData.getName());
            }
            if (newData.getPhone() != null) {
                managed.setPhone(newData.getPhone());
            }
            if (newData.getRole() != null) {
                managed.setRole(newData.getRole());
            }
            if (newData.getPassword() != null) {
                managed.setPassword(newData.getPassword());
            }
            if (newData.getBranch() != null) {
                managed.setBranch(newData.getBranch());
            }
            if (newData.isStatus() != managed.isStatus()) {
                managed.setStatus(newData.isStatus());
            }

            return managed; // Hibernate flushes automatically
        });
    }

    @Override
    public EmployeeModel delete(String id) {
        return execute(session -> {
            EmployeeModel managed = session.get(EmployeeModel.class, id);
            if (managed == null) {
                return null;
            }

            session.remove(managed);
            return managed;
        });
    }

    @Override
    public EmployeeModel findById(String id) {
        return execute(session -> session.get(EmployeeModel.class, id));
    }

    @Override
    public List<EmployeeModel> findAll() {
        return execute(session
                -> session.createQuery(
                        "FROM EmployeeModel e ORDER BY e.startAt DESC", EmployeeModel.class
                ).setMaxResults(50).list()
        );
    }

    @Override
    public boolean existsByUsername(String username) {
        return execute(session -> {
            Query<Integer> query = session.createQuery(
                    "SELECT 1 FROM EmployeeModel e WHERE e.username = :username", Integer.class
            );
            query.setParameter("username", username);
            query.setMaxResults(1);
            return query.uniqueResult() != null;
        });
    }

    @Override
    public EmployeeModel findByUsername(String username) {
        return execute(session -> {
            Query<EmployeeModel> query = session.createQuery(
                    "SELECT e FROM EmployeeModel e "
                    + "LEFT JOIN FETCH e.branch "
                    + "WHERE e.username = :username",
                    EmployeeModel.class
            );
            query.setParameter("username", username);
            return query.uniqueResult();
        });
    }

    @Override
    public List<EmployeeModel> findActive() {
        return execute(session
                -> session.createQuery(
                        "FROM EmployeeModel e WHERE e.status = true ORDER BY e.startAt DESC",
                        EmployeeModel.class
                ).list()
        );
    }

    @Override
    public List<EmployeeModel> filter(Map<String, Object> filters) {
        return execute(session -> {
            StringBuilder hql = new StringBuilder("FROM EmployeeModel e WHERE 1=1 ");

            if (filters.containsKey("branchId")) {
                hql.append("AND e.branch.id = :branchId ");
            }
            if (filters.containsKey("role")) {
                hql.append("AND e.role = :role ");
            }
            if (filters.containsKey("status")) {
                hql.append("AND e.status = :status ");
            }

            Query<EmployeeModel> query = session.createQuery(hql.toString(), EmployeeModel.class);

            if (filters.containsKey("branchId")) {
                query.setParameter("branchId", filters.get("branchId"));
            }

            if (filters.containsKey("role")) {
                Object r = filters.get("role");
                EmployeeModel.Role roleEnum = null;

                if (r instanceof String s) {
                    roleEnum = EmployeeModel.Role.valueOf(s.toUpperCase());
                } else if (r instanceof EmployeeModel.Role e) {
                    roleEnum = e;
                }

                query.setParameter("role", roleEnum);
            }

            if (filters.containsKey("status")) {
                query.setParameter("status", filters.get("status"));
            }

            return query.list();
        });
    }
}
