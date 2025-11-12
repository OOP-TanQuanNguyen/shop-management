package edu.ptithcm.repository.branch;

import edu.ptithcm.models.BranchModel;
import edu.ptithcm.repository.BaseRepository;

public class BranchRepositoryImpl extends BaseRepository<BranchModel> implements BranchRepository {

    @Override
    public void save(BranchModel entity) { execute(s -> { s.persist(entity); return null; }); }

    @Override
    public void update(BranchModel entity) { execute(s -> { s.merge(entity); return null; }); }

    @Override
    public void delete(BranchModel entity) { execute(s -> { s.remove(entity); return null; }); }

    @Override
    public BranchModel findById(Integer id) { return execute(s -> s.get(BranchModel.class, id)); }

    @Override
    public java.util.List<BranchModel> findAll() {
        return execute(s -> s.createQuery("FROM BranchModel ORDER BY name", BranchModel.class).list());
    }
}
