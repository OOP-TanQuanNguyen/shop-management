package edu.ptithcm.repository.employee;

import edu.ptithcm.models.EmployeeModel;
import edu.ptithcm.repository.GenericRepository;
import java.util.List;
import java.util.Map;

/**
 * Repository interface for EmployeeModel.
 * Defines custom query operations beyond basic CRUD.
 */
public interface EmployeeRepository extends GenericRepository<EmployeeModel, String> {

    boolean existsByUsername(String username);

    EmployeeModel findByUsername(String username);

    List<EmployeeModel> findActive();

    List<EmployeeModel> filter(Map<String, Object> filters);
}
