package edu.ptithcm.controller;

import edu.ptithcm.dto.request.customer.CustomerRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.CustomerInfo;
import edu.ptithcm.services.CustomerService;

import java.util.List;

public class CustomerController {

    private final CustomerService service = new CustomerService();

    public ResponseDTO<List<CustomerInfo>> getAllCustomers() {
        return service.getAllCustomers();
    }

    public ResponseDTO<CustomerInfo> createCustomer(CustomerRequestDTO req) {
        return service.createCustomer(req);
    }

    public ResponseDTO<CustomerInfo> updateCustomer(CustomerRequestDTO req) {
        return service.updateCustomer(req);
    }

    public ResponseDTO<CustomerInfo> deleteCustomer(CustomerRequestDTO req) {
        return service.deleteCustomer(req);
    }

    public ResponseDTO<CustomerInfo> getCustomerById(CustomerRequestDTO req) {
        return service.getCustomerById(req);
    }

    public ResponseDTO<CustomerInfo> getCustomerByPhone(String phone) {
        return service.getCustomerByPhone(phone);
    }
}
