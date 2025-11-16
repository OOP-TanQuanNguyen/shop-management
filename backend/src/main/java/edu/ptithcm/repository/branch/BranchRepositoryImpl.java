package edu.ptithcm.repository.branch;

import java.util.List;

import edu.ptithcm.models.BranchModel;
import edu.ptithcm.repository.BaseRepository;

public class BranchRepositoryImpl extends BaseRepository<BranchModel> implements BranchRepository {

    @Override
    public void save(BranchModel entity) {
        execute(session -> {
            session.persist(entity);
            return null;
        });
    }

    @Override
    public BranchModel update(BranchModel newData) {
        return execute(session -> {
            BranchModel managed = session.get(BranchModel.class, newData.getId());
            if (managed == null) return null;

            // Áp dụng update an toàn (dirty checking)
            if (newData.getName() != null) managed.setName(newData.getName());
            if (newData.getAddress() != null) managed.setAddress(newData.getAddress());
            if (newData.getPhone() != null) managed.setPhone(newData.getPhone());
            if (newData.isActive() != managed.isActive()) managed.setActive(newData.isActive());

            return managed; // Hibernate auto-flush khi commit
        });
    }

    @Override
    public BranchModel delete(Integer id) {
        return execute(session -> {
            BranchModel managed = session.get(BranchModel.class, id);
            if (managed == null) return null;
            session.remove(managed);
            return managed; // trả về cho service hiển thị nếu cần
        });
    }

    @Override
    public BranchModel findById(Integer id) {
        return execute(session -> session.get(BranchModel.class, id));
    }

    @Override
    public List<BranchModel> findAll() {
        return execute(session ->
            session.createQuery("FROM BranchModel b ORDER BY b.name ASC", BranchModel.class).list()
        );
    }

}
