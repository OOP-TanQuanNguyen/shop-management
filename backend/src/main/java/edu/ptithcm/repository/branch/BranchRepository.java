package edu.ptithcm.repository.branch;

import java.sql.SQLException;
import java.util.List;

import edu.ptithcm.model.BranchModel;

public interface BranchRepository {

    void create(BranchModel branch) throws SQLException;

    List<BranchModel> getAll() throws SQLException;

    BranchModel findById(int id) throws SQLException;

    void update(int id, BranchModel branch) throws SQLException;

    void remove(int id) throws SQLException;
}
