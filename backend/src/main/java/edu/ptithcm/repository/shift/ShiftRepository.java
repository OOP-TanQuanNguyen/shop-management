package edu.ptithcm.repository.shift;

import java.sql.SQLException;
import java.util.List;
import edu.ptithcm.model.ShiftModel;

public interface ShiftRepository {
    void create(ShiftModel shift) throws SQLException;
    List<ShiftModel> getAll() throws SQLException;
    ShiftModel findById(int id) throws SQLException;
    void update(int id, ShiftModel shift) throws SQLException;
    void remove(int id) throws SQLException;
}
