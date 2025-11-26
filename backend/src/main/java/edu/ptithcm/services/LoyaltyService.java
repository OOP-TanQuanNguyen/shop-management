package edu.ptithcm.services;

import java.util.List;
import java.util.UUID;
import java.sql.Timestamp;
import java.time.Instant;

import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.dto.response.info_models.LoyaltyInfo;
import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.models.LoyaltyModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.customer.CustomerRepository;
import edu.ptithcm.repository.loyalty.LoyaltyRepository;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;

public class LoyaltyService {

    private static final LoyaltyRepository loyaltyRepo = Repository.loyalty();
    private static final CustomerRepository customerRepo = Repository.customer();
    private static final BaseMapper<LoyaltyModel, LoyaltyInfo> mapper = MapperFactory.loyalty();

    public ResponseDTO<List<LoyaltyInfo>> getAllLoyalty() throws RuntimeException {
        return new SuccessResponse<>("Lấy toàn bộ loyalty thành công",
                mapper.toDTOList(loyaltyRepo.findAll()));
    }

    public ResponseDTO<LoyaltyInfo> getLoyaltyByCustomer(String customerId) throws RuntimeException {
        if (customerId == null || customerId.isBlank())
            return new InvalidResponse<>("Thiếu ID khách hàng");

        LoyaltyModel loyalty = loyaltyRepo.findByCustomerId(customerId);
        if (loyalty == null)
            return new NotFoundResponse<>("Không tìm thấy thông tin loyalty");

        return new SuccessResponse<>("Lấy loyalty thành công", mapper.toDTO(loyalty));
    }

    public ResponseDTO<LoyaltyInfo> createLoyalty(String customerId) throws RuntimeException {
        if (customerId == null || customerId.isBlank()) 
            return new InvalidResponse<>("Thiếu ID khách hàng");

        CustomerModel customer = customerRepo.findById(customerId);
        if (customer == null) 
            return new NotFoundResponse<>("Không tìm thấy khách hàng");

        LoyaltyModel existing = loyaltyRepo.findByCustomerId(customerId);
        if (existing != null) 
            return new InvalidResponse<>("Khách hàng đã có thông tin");

        LoyaltyModel loyalty = new LoyaltyModel();
        loyalty.setId(UUID.randomUUID().toString());
        loyalty.setCustomer(customer);
        loyalty.setTotalPoints(0);
        loyalty.setLastUpdate(Timestamp.from(Instant.now()));

        loyaltyRepo.save(loyalty);

        return new SuccessResponse<>("Tạo loyalty thành công", mapper.toDTO(loyalty));
    }

    public ResponseDTO<LoyaltyInfo> updateLoyalty(String customerId, int pointsChange) throws RuntimeException {
        if (customerId == null || customerId.isBlank()) 
            return new InvalidResponse<>("Thiếu ID khách hàng");
        LoyaltyModel loyalty = loyaltyRepo.findByCustomerId(customerId);
        if (loyalty == null) 
            return new InvalidResponse<>("Không tìm thấy loyalty của khách hàng");

        int newPoints = loyalty.getTotalPoints() + pointsChange;

        if (newPoints < 0) 
            return new InvalidResponse<>("Điểm không thể âm");

        loyalty.setTotalPoints(newPoints); 
        loyalty.setLastUpdate(Timestamp.from(Instant.now()));

        LoyaltyModel updated = loyaltyRepo.update(loyalty);
        if (updated == null)
            return new NotFoundResponse<>("Không tìm thấy loyalty để cập nhật");

        return new SuccessResponse<>("Cập nhật loyalty thành công", mapper.toDTO(updated));
    }

    public ResponseDTO<LoyaltyInfo> deleteLoyalty(String customerId) throws RuntimeException {
        if (customerId == null || customerId.isBlank()) 
            return new InvalidResponse<>("Thiếu ID khách hàng");

        LoyaltyModel loyalty = loyaltyRepo.findByCustomerId(customerId);
        if (loyalty == null)
            return new SuccessResponse<>("Không có loyalty để xóa", null);
        
        LoyaltyModel deleted = loyaltyRepo.delete(loyalty.getId());

        if (deleted == null)
            return new NotFoundResponse<>("Không tìm thấy loyalty để xóa");

        return new SuccessResponse<>("Xóa loyalty thành công", mapper.toDTO(deleted));
    }
}
