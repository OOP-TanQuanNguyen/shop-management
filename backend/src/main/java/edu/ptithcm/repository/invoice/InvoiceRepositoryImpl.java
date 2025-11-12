package edu.ptithcm.repository.invoice;

import edu.ptithcm.models.InvoiceModel;
import edu.ptithcm.repository.BaseRepository;
import org.hibernate.query.Query;
import java.util.List;

public class InvoiceRepositoryImpl extends BaseRepository<InvoiceModel> implements InvoiceRepository {

    @Override
    public void save(InvoiceModel entity) { execute(s -> { s.persist(entity); return null; }); }

    @Override
    public void update(InvoiceModel entity) { execute(s -> { s.merge(entity); return null; }); }

    @Override
    public void delete(InvoiceModel entity) { execute(s -> { s.remove(entity); return null; }); }

    @Override
    public InvoiceModel findById(String id) { return execute(s -> s.get(InvoiceModel.class, id)); }

    @Override
    public List<InvoiceModel> findAll() {
        return execute(s ->
            s.createQuery("FROM InvoiceModel i ORDER BY i.createdAt DESC", InvoiceModel.class).list());
    }

    @Override
    public List<InvoiceModel> findByCustomer(String customerId) {
        return execute(s -> {
            Query<InvoiceModel> q = s.createQuery(
                "FROM InvoiceModel i WHERE i.customer.id = :cid ORDER BY i.createdAt DESC", InvoiceModel.class);
            q.setParameter("cid", customerId);
            return q.list();
        });
    }

    @Override
    public List<InvoiceModel> findByBranch(Integer branchId) {
        return execute(s -> {
            Query<InvoiceModel> q = s.createQuery(
                "FROM InvoiceModel i WHERE i.branch.id = :bid ORDER BY i.createdAt DESC", InvoiceModel.class);
            q.setParameter("bid", branchId);
            return q.list();
        });
    }
}
