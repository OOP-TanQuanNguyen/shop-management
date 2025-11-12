package edu.ptithcm.repository.customer;

import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.repository.BaseRepository;
import org.hibernate.query.Query;

public class CustomerRepositoryImpl extends BaseRepository<CustomerModel> implements CustomerRepository {

    @Override
    public void save(CustomerModel entity) { execute(s -> { s.persist(entity); return null; }); }

    @Override
    public void update(CustomerModel entity) { execute(s -> { s.merge(entity); return null; }); }

    @Override
    public void delete(CustomerModel entity) { execute(s -> { s.remove(entity); return null; }); }

    @Override
    public CustomerModel findById(String id) { return execute(s -> s.get(CustomerModel.class, id)); }

    @Override
    public java.util.List<CustomerModel> findAll() {
        return execute(s -> s.createQuery("FROM CustomerModel ORDER BY createdAt DESC", CustomerModel.class).list());
    }

    @Override
    public CustomerModel findByPhone(String phone) {
        return execute(s -> {
            Query<CustomerModel> q = s.createQuery(
                "FROM CustomerModel c WHERE c.phone = :phone", CustomerModel.class);
            q.setParameter("phone", phone);
            return q.uniqueResult();
        });
    }
}
