package edu.ptithcm.services;

import java.util.List;

import edu.ptithcm.dto.request.branch.BranchRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.dto.response.info_models.BranchInfo;
import edu.ptithcm.models.BranchModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.branch.BranchRepository;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;

public class BranchService {

    private static final BranchRepository branchRepo = Repository.branch();
    private static final BaseMapper<BranchModel, BranchInfo> mapper = MapperFactory.branch();

    // Lấy tất cả chi nhánh
    public ResponseDTO<List<BranchInfo>> getAllBranches() throws RuntimeException {
        return new SuccessResponse<>(
                "Lấy toàn bộ chi nhánh thành công",
                mapper.toDTOList(branchRepo.findAll())
        );
    }

    // Tạo chi nhánh
    public ResponseDTO<BranchInfo> createBranch(BranchRequestDTO req) throws RuntimeException {
        if (!req.validForCreate()) {
            return new InvalidResponse<>("Thiếu tên chi nhánh");
        }

        BranchModel branch = new BranchModel.Builder()
                .name(req.getName())
                .phone(req.getPhone())
                .address(req.getAddress())
                .isActive(req.getIsActive() != null ? req.getIsActive() : true)
                .build();

        branchRepo.save(branch);

        return new SuccessResponse<>("Tạo chi nhánh thành công", mapper.toDTO(branch));
    }

    // Cập nhật chi nhánh
    public ResponseDTO<BranchInfo> updateBranch(BranchRequestDTO req) throws RuntimeException {
        if (!req.validForUpdate()) {
            return new InvalidResponse<>("Thiếu ID hoặc tên chi nhánh");
        }

        BranchModel temp = new BranchModel.Builder()
                .id(req.getBranchId())
                .name(req.getName())
                .phone(req.getPhone())
                .address(req.getAddress())
                .isActive(req.getIsActive() != null ? req.getIsActive() : true)
                .build();

        BranchModel updated = branchRepo.update(temp);

        if (updated == null) {
            return new NotFoundResponse<>("Không tìm thấy chi nhánh để cập nhật");
        }

        return new SuccessResponse<>("Cập nhật chi nhánh thành công", mapper.toDTO(updated));
    }

    // Xóa chi nhánh
    public ResponseDTO<BranchInfo> deleteBranch(BranchRequestDTO req) throws RuntimeException {
        if (req.getBranchId() == null || req.getBranchId() <= 0) {
            return new InvalidResponse<>("Thiếu ID chi nhánh");
        }

        BranchModel deleted = branchRepo.delete(req.getBranchId());

        if (deleted == null) {
            return new NotFoundResponse<>("Không tồn tại chi nhánh");
        }

        return new SuccessResponse<>("Xóa chi nhánh thành công", mapper.toDTO(deleted));
    }

    // Lấy chi nhánh theo ID
    public ResponseDTO<BranchInfo> getBranchesById(BranchRequestDTO req) throws RuntimeException {
        BranchModel branch = branchRepo.findById(req.getBranchId());

        if (branch == null) {
            return new NotFoundResponse<>("Không tìm thấy chi nhánh");
        }

        return new SuccessResponse<>("Lấy chi nhánh thành công", mapper.toDTO(branch));
    }
}
