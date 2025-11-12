package edu.ptithcm.repository.customer;

import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.repository.GenericRepository;

public interface CustomerRepository extends GenericRepository<CustomerModel, String> {
    CustomerModel findByPhone(String phone);
}
