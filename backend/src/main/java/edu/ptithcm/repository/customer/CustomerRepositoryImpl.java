package edu.ptithcm.repository.customer;

import java.util.List;

import org.hibernate.query.Query;

import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.repository.BaseRepository;

public class CustomerRepositoryImpl extends BaseRepository<CustomerModel> implements CustomerRepository {

    @Override
    public void save(CustomerModel entity) {
        execute(session -> {
            session.persist(entity);
            return null;
        });
    }

    @Override
    public CustomerModel update(CustomerModel newData) {
        return execute(session -> {
            CustomerModel managed = session.get(CustomerModel.class, newData.getId());
            if (managed == null) return null;

            // dirty checking update — chỉ set field thay đổi
            if (newData.getName() != null) managed.setName(newData.getName());
            if (newData.getPhone() != null) managed.setPhone(newData.getPhone());

            return managed; // Hibernate tự flush khi commit
        });
    }

    @Override
    public CustomerModel delete(String id) {
        return execute(session -> {
            CustomerModel managed = session.get(CustomerModel.class, id);
            if (managed == null) return null;
            session.remove(managed);
            return managed;
        });
    }

    @Override
    public CustomerModel findById(String id) {
        return execute(session -> session.get(CustomerModel.class, id));
    }

    @Override
    public List<CustomerModel> findAll() {
        return execute(session ->
            session.createQuery(
                "FROM CustomerModel c ORDER BY c.createdAt DESC", CustomerModel.class
            ).list()
        );
    }

    @Override
    public CustomerModel findByPhone(String phone) {
        return execute(session -> {
            Query<CustomerModel> q = session.createQuery(
                "FROM CustomerModel c WHERE c.phone = :phone", CustomerModel.class);
            q.setParameter("phone", phone);
            q.setMaxResults(1);
            return q.uniqueResult();
        });
    }

    public List<CustomerModel> findActive() {
        return execute(session ->
            session.createQuery(
                "FROM CustomerModel c WHERE c.active = true ORDER BY c.name ASC",
                CustomerModel.class
            ).list()
        );
    }
}
