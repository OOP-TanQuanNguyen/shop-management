package edu.ptithcm.controller;

import edu.ptithcm.dto.request.customer.CustomerRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.CustomerInfo;
import edu.ptithcm.services.CustomerService;
import edu.ptithcm.utils.handle_exception.SafeExecutor;

import java.util.List;

public class CustomerController {

    private final CustomerService service = new CustomerService();

    public ResponseDTO<List<CustomerInfo>> getAllCustomers() {
        return SafeExecutor.run(() -> service.getAllCustomers());
    }

    public ResponseDTO<CustomerInfo> createCustomer(CustomerRequestDTO req, String sessionId) {
        return SafeExecutor.run(() -> service.createCustomer(req, sessionId));
    }

    public ResponseDTO<CustomerInfo> updateCustomer(CustomerRequestDTO req, String sessionId) {
        return SafeExecutor.run(() -> service.updateCustomer(req, sessionId));
    }

    public ResponseDTO<CustomerInfo> deleteCustomer(CustomerRequestDTO req) {
        return SafeExecutor.run(() -> service.deleteCustomer(req));
    }

    public ResponseDTO<CustomerInfo> getCustomerById(CustomerRequestDTO req) {
        return SafeExecutor.run(() -> service.getCustomerById(req));
    }

    public ResponseDTO<CustomerInfo> getCustomerByPhone(String phone) {
        return SafeExecutor.run(() -> service.getCustomerByPhone(phone));
    }
}
