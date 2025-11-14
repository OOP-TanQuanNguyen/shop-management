package edu.ptithcm.repository.employee;

import java.util.List;
import java.util.Map;

import edu.ptithcm.models.EmployeeModel;
import edu.ptithcm.repository.GenericRepository;

public interface EmployeeRepository extends GenericRepository<EmployeeModel, String> {

    boolean existsByUsername(String username);

    EmployeeModel findByUsername(String username);

    List<EmployeeModel> findActive();

    List<EmployeeModel> filter(Map<String, Object> filters);
}
