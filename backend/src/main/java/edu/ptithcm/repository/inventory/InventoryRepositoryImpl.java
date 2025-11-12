package edu.ptithcm.repository.inventory;

import edu.ptithcm.models.InventoryModel;
import edu.ptithcm.repository.BaseRepository;
import org.hibernate.query.Query;
import java.util.List;

public class InventoryRepositoryImpl extends BaseRepository<InventoryModel> implements InventoryRepository {

    @Override
    public void save(InventoryModel entity) {
        execute(s -> { s.persist(entity); return null; });
    }

    @Override
    public void update(InventoryModel entity) {
        execute(s -> { s.merge(entity); return null; });
    }

    @Override
    public void delete(InventoryModel entity) {
        execute(s -> { s.remove(entity); return null; });
    }

    @Override
    public InventoryModel findById(Integer id) {
        return execute(s -> s.get(InventoryModel.class, id));
    }

    @Override
    public List<InventoryModel> findAll() {
        return execute(s -> s.createQuery(
                "FROM InventoryModel i ORDER BY i.updatedAt DESC", InventoryModel.class)
                .list());
    }

    @Override
    public List<InventoryModel> findByBranch(Integer branchId) {
        return execute(s -> {
            Query<InventoryModel> q = s.createQuery(
                "FROM InventoryModel i WHERE i.branch.id = :bid ORDER BY i.updatedAt DESC",
                InventoryModel.class);
            q.setParameter("bid", branchId);
            return q.list();
        });
    }

    @Override
    public InventoryModel findByBranchAndProduct(Integer branchId, String productId) {
        return execute(s -> {
            Query<InventoryModel> q = s.createQuery(
                "FROM InventoryModel i WHERE i.branch.id = :bid AND i.product.id = :pid",
                InventoryModel.class);
            q.setParameter("bid", branchId);
            q.setParameter("pid", productId);
            return q.uniqueResult();
        });
    }
}
