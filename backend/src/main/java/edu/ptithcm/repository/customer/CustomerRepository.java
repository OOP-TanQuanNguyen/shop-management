package edu.ptithcm.repository.customer;

import java.sql.SQLException;
import java.util.List;

import edu.ptithcm.model.CustomerModel;

public interface CustomerRepository {
    void create(CustomerModel customer) throws SQLException;
    void update(String id, String name, String phone) throws SQLException;
    void remove(String id) throws SQLException;
    boolean exists(String phone) throws SQLException;
    CustomerModel findById(String id) throws SQLException;
    List<CustomerModel> getAll(int limit) throws SQLException;
    List<CustomerModel> search(String keyword) throws SQLException;
}
