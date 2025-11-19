package edu.ptithcm.controller;

import java.util.List;

import edu.ptithcm.dto.request.branch.BranchRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.BranchInfo;
import edu.ptithcm.services.BranchService;
import edu.ptithcm.utils.handle_exception.SafeExecutor;

public class BranchController {

    private final BranchService service = new BranchService();

    public ResponseDTO<List<BranchInfo>> getAllBranches() {
        return SafeExecutor.run(() -> service.getAllBranches());
    }

    public ResponseDTO<BranchInfo> createBranch(BranchRequestDTO req) {
        return SafeExecutor.run(() -> service.createBranch(req));
    }

    public ResponseDTO<BranchInfo> updateBranch(BranchRequestDTO req) {
        return SafeExecutor.run(() -> service.updateBranch(req));
    }

    public ResponseDTO<BranchInfo> deleteBranch(BranchRequestDTO req) {
        return SafeExecutor.run(() -> service.deleteBranch(req));
    }

    public ResponseDTO<BranchInfo> getBranchesById(BranchRequestDTO req) {
        return SafeExecutor.run(() -> service.getBranchesById(req));
    }
}
