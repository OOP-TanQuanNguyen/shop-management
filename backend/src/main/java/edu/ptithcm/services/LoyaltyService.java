package edu.ptithcm.services;

import java.util.List;
import java.util.UUID;
import java.sql.Timestamp;

import edu.ptithcm.dto.request.loyalty.LoyaltyRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.dto.response.info_models.LoyaltyInfo;
import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.models.LoyaltyModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.loyalty.LoyaltyRepository;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;

public class LoyaltyService {

    private static final LoyaltyRepository loyaltyRepo = Repository.loyalty();
    private static final BaseMapper<LoyaltyModel, LoyaltyInfo> mapper = MapperFactory.loyalty();

    public ResponseDTO<List<LoyaltyInfo>> getAllLoyalty() throws RuntimeException {
        return new SuccessResponse<>("Lấy toàn bộ loyalty thành công",
                mapper.toDTOList(loyaltyRepo.findAll()));
    }

    public ResponseDTO<LoyaltyInfo> getLoyaltyByCustomer(LoyaltyRequestDTO req) throws RuntimeException {
        if (req.getCustomerId() == null || req.getCustomerId().isBlank())
            return new InvalidResponse<>("Thiếu ID khách hàng");

        LoyaltyModel loyalty = loyaltyRepo.findByCustomerId(req.getCustomerId());
        if (loyalty == null)
            return new NotFoundResponse<>("Không tìm thấy thông tin loyalty");

        return new SuccessResponse<>("Lấy loyalty thành công", mapper.toDTO(loyalty));
    }

    public ResponseDTO<LoyaltyInfo> createLoyalty(LoyaltyRequestDTO req) throws RuntimeException {
        if (req.getCustomerId() == null || req.getCustomerId().isBlank())
            return new InvalidResponse<>("Thiếu ID khách hàng");

        CustomerModel customer = Repository.customer().findById(req.getCustomerId());
        if (customer == null)
            return new NotFoundResponse<>("Không tìm thấy khách hàng");

        LoyaltyModel loyalty = new LoyaltyModel();
        loyalty.setId(UUID.randomUUID().toString());
        loyalty.setCustomer(customer);
        loyalty.setTotalPoints(req.getTotalPoints() != null ? req.getTotalPoints() : 0);
        loyalty.setLastUpdate(new Timestamp(System.currentTimeMillis()));

        loyaltyRepo.save(loyalty);

        return new SuccessResponse<>("Tạo loyalty thành công", mapper.toDTO(loyalty));
    }

    public ResponseDTO<LoyaltyInfo> updateLoyalty(LoyaltyRequestDTO req) throws RuntimeException {
        if (req.getLoyaltyId() == null || req.getLoyaltyId().isBlank())
            return new InvalidResponse<>("Thiếu ID loyalty");

        LoyaltyModel temp = new LoyaltyModel();
        temp.setId(req.getLoyaltyId());
        temp.setTotalPoints(req.getTotalPoints() != null ? req.getTotalPoints() : 0);
        temp.setLastUpdate(new Timestamp(System.currentTimeMillis()));

        LoyaltyModel updated = loyaltyRepo.update(temp);
        if (updated == null)
            return new NotFoundResponse<>("Không tìm thấy loyalty để cập nhật");

        return new SuccessResponse<>("Cập nhật loyalty thành công", mapper.toDTO(updated));
    }

    public ResponseDTO<LoyaltyInfo> deleteLoyalty(LoyaltyRequestDTO req) throws RuntimeException {
        if (req.getLoyaltyId() == null || req.getLoyaltyId().isBlank())
            return new InvalidResponse<>("Thiếu ID loyalty");

        LoyaltyModel deleted = loyaltyRepo.delete(req.getLoyaltyId());
        if (deleted == null)
            return new NotFoundResponse<>("Không tìm thấy loyalty để xóa");

        return new SuccessResponse<>("Xóa loyalty thành công", mapper.toDTO(deleted));
    }
}
