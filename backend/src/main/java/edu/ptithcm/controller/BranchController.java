package edu.ptithcm.controller;

import java.util.List;

import edu.ptithcm.dto.request.branch.BranchRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.BranchInfo;
import edu.ptithcm.services.BranchService;

public class BranchController {

    private final BranchService service = new BranchService();

    // Lấy tất cả chi nhánh
    public ResponseDTO<List<BranchInfo>> getAllBranches() {
        return service.getAllBranches();
    }

    // Tạo chi nhánh mới
    public ResponseDTO<BranchInfo> createBranch(BranchRequestDTO req) {
        return service.createBranch(req);
    }

    // Cập nhật chi nhánh
    public ResponseDTO<BranchInfo> updateBranch(BranchRequestDTO req) {
        return service.updateBranch(req);
    }

    // Xóa chi nhánh
    public ResponseDTO<BranchInfo> deleteBranch(BranchRequestDTO req) {
        return service.deleteBranch(req);
    }

    // Lấy chi nhánh theo ID
    public ResponseDTO<BranchInfo> getBranchById(BranchRequestDTO req) {
        return service.getBranchById(req);
    }
}
