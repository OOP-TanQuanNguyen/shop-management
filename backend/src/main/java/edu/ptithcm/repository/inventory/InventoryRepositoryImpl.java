package edu.ptithcm.repository.inventory;

import java.util.List;

import org.hibernate.query.Query;

import edu.ptithcm.models.InventoryModel;
import edu.ptithcm.repository.BaseRepository;

public class InventoryRepositoryImpl extends BaseRepository<InventoryModel> implements InventoryRepository {

    @Override
    public void save(InventoryModel entity) {
        execute(session -> {
            session.persist(entity);
            return null;
        });
    }

    @Override
    public InventoryModel update(InventoryModel newData) {
        return execute(session -> {
            InventoryModel managed = session.get(InventoryModel.class, newData.getId());
            if (managed == null) return null;

            if (newData.getQuantity() != 0) managed.setQuantity(newData.getQuantity());
            if (newData.getBranch() != null) managed.setBranch(newData.getBranch());
            if (newData.getProduct() != null) managed.setProduct(newData.getProduct());

            return managed;
        });
    }

    @Override
    public InventoryModel delete(Integer id) {
        return execute(session -> {
            InventoryModel managed = session.get(InventoryModel.class, id);
            if (managed == null) return null;
            session.remove(managed);
            return managed;
        });
    }

    @Override
    public InventoryModel findById(Integer id) {
        return execute(session -> session.get(InventoryModel.class, id));
    }

    @Override
    public List<InventoryModel> findAll() {
        return execute(session ->
            session.createQuery(
                "FROM InventoryModel i ORDER BY i.updatedAt DESC", InventoryModel.class
            ).list()
        );
    }

    @Override
    public List<InventoryModel> findByBranch(Integer branchId) {
        return execute(session -> {
            Query<InventoryModel> query = session.createQuery(
                "FROM InventoryModel i " +
                "WHERE i.branch.id = :branchId " +
                "ORDER BY i.updatedAt DESC", InventoryModel.class);
            query.setParameter("branchId", branchId);
            return query.list();
        });
    }

    @Override
    public InventoryModel findByBranchAndProduct(Integer branchId, String productId) {
        return execute(session -> session.createQuery(
                "FROM InventoryModel i WHERE i.branch.id = :branchId AND i.product.id = :productId",
                InventoryModel.class
            )
            .setParameter("branchId", branchId)
            .setParameter("productId", productId)
            .setMaxResults(1)
            .uniqueResult()
        );
    }
}