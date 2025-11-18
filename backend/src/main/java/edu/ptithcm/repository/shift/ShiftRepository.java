package edu.ptithcm.repository.shift;

import java.util.List;

import edu.ptithcm.models.ShiftModel;
import edu.ptithcm.repository.GenericRepository;

public interface ShiftRepository extends GenericRepository<ShiftModel, Integer> {
    List<ShiftModel> findByName(String name);
}
