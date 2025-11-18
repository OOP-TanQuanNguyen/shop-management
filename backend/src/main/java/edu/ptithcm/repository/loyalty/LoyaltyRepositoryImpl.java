package edu.ptithcm.repository.loyalty;

import org.hibernate.query.Query;
import java.util.List;
import edu.ptithcm.models.LoyaltyModel;
import edu.ptithcm.repository.BaseRepository;

public class LoyaltyRepositoryImpl extends BaseRepository<LoyaltyModel> implements LoyaltyRepository {

    @Override
    public void save(LoyaltyModel entity) {
        execute(session -> {
            session.persist(entity);
            return null;
        });
    }

    @Override
    public LoyaltyModel update(LoyaltyModel newData) {
        return execute(session -> {
            LoyaltyModel managed = session.get(LoyaltyModel.class, newData.getId());
            if (managed == null) return null;
            managed.setTotalPoints(newData.getTotalPoints());
            managed.setLastUpdate(newData.getLastUpdate());
            return managed;
        });
    }

    @Override
    public LoyaltyModel delete(String id) {
        return execute(session -> {
            LoyaltyModel managed = session.get(LoyaltyModel.class, id);
            if (managed == null) return null;
            session.remove(managed);
            return managed;
        });
    }

    @Override
    public List<LoyaltyModel> findAll() {
        return execute(session -> session.createQuery(
            "FROM LoyaltyModel l ORDER BY l.lastUpdate DESC", LoyaltyModel.class
        ).list());
    }


    @Override
    public LoyaltyModel findById(String id) {
        return execute(session -> session.get(LoyaltyModel.class, id));
    }

    @Override
    public LoyaltyModel findByCustomerId(String customerId) {
        return execute(session -> {
            Query<LoyaltyModel> query = session.createQuery(
                "FROM LoyaltyModel l WHERE l.customer.id = :cid", LoyaltyModel.class
            );
            query.setParameter("cid", customerId);
            return query.uniqueResult();
        });
    }
}
