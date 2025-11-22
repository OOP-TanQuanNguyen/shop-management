package edu.ptithcm.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import edu.ptithcm.dto.request.customer.CustomerRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.dto.response.info_models.CustomerInfo;
import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.repository.customer.CustomerRepository;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;

public class CustomerService {

    private static final CustomerRepository customerRepo = Repository.customer();
    private static final BaseMapper<CustomerModel, CustomerInfo> mapper = MapperFactory.customer();

    // ------------------ Lấy toàn bộ ------------------
    public ResponseDTO<List<CustomerInfo>> getAllCustomers() throws RuntimeException {
        List<CustomerModel> list = customerRepo.findAll();
        return new SuccessResponse<>("Lấy toàn bộ khách hàng thành công", mapper.toDTOList(list));
    }

    // ------------------ Tạo khách hàng ------------------
    public ResponseDTO<CustomerInfo> createCustomer(CustomerRequestDTO req) throws RuntimeException {

        if (!req.validForCreate())
            return new InvalidResponse<>("Thiếu tên khách hàng");

        CustomerModel customer = new CustomerModel();
        customer.setId(UUID.randomUUID().toString());
        customer.setName(req.getName());
        customer.setPhone(req.getPhone());
        customer.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        customerRepo.save(customer);

        return new SuccessResponse<>("Tạo khách hàng thành công", mapper.toDTO(customer));
    }

    // ------------------ Cập nhật khách hàng ------------------
    public ResponseDTO<CustomerInfo> updateCustomer(CustomerRequestDTO req) throws RuntimeException {

        if (!req.validForUpdate())
            return new InvalidResponse<>("Thiếu ID khách hàng");

        CustomerModel existing = customerRepo.findById(req.getCustomerId());
        if (existing == null)
            return new NotFoundResponse<>("Không tìm thấy khách hàng để cập nhật");

        if (req.getName() != null) existing.setName(req.getName());
        if (req.getPhone() != null) existing.setPhone(req.getPhone());

        CustomerModel updated = customerRepo.update(existing);

        return new SuccessResponse<>("Cập nhật khách hàng thành công", mapper.toDTO(updated));
    }

    // ------------------ Xóa khách hàng ------------------
    public ResponseDTO<CustomerInfo> deleteCustomer(CustomerRequestDTO req) throws RuntimeException {

        if (req.getCustomerId() == null || req.getCustomerId().isBlank())
            return new InvalidResponse<>("Thiếu ID khách hàng");

        CustomerModel deleted = customerRepo.delete(req.getCustomerId());

        if (deleted == null)
            return new NotFoundResponse<>("Không tồn tại khách hàng");

        return new SuccessResponse<>("Xóa khách hàng thành công", mapper.toDTO(deleted));
    }

    // ------------------ Lấy khách hàng theo ID ------------------
    public ResponseDTO<CustomerInfo> getCustomerById(CustomerRequestDTO req) throws RuntimeException {

        CustomerModel customer = customerRepo.findById(req.getCustomerId());

        if (customer == null)
            return new NotFoundResponse<>("Không tìm thấy khách hàng");

        return new SuccessResponse<>("Lấy khách hàng thành công", mapper.toDTO(customer));
    }

    // ------------------ Lấy khách hàng theo số điện thoại ------------------
    public ResponseDTO<CustomerInfo> getCustomerByPhone(String phone) throws RuntimeException {

        if (phone == null || phone.isBlank())
            return new InvalidResponse<>("Thiếu số điện thoại");

        CustomerModel customer = customerRepo.findByPhone(phone);

        if (customer == null)
            return new NotFoundResponse<>("Không tìm thấy khách hàng");

        return new SuccessResponse<>("Lấy khách hàng theo số điện thoại thành công", mapper.toDTO(customer));
    }
}
