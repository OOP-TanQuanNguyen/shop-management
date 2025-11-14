package edu.ptithcm.routes.branch;

import java.util.List;
import java.util.Map;

import edu.ptithcm.configs.Role;
import edu.ptithcm.configs.TypeDTTP;
import edu.ptithcm.controller.BranchController;
import edu.ptithcm.dto.request.branch.BranchRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.BranchInfo;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.utils.ReplyUtils;

public class BranchRoute {

    private final DTTP server;
    private final DTTPStateManager manager;
    private final BranchController controller;

    public BranchRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
        this.controller = new BranchController();
    }

    public void register() {
        // ---------------- GET ALL ----------------
        server.on(TypeDTTP.BRANCH_GET_ALL.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.BRANCH_GET_ALL.getValue())) return;

                ResponseDTO<List<BranchInfo>> response = controller.getAllBranches();
                List<Map<String, Object>> branches = response.getData() != null
                        ? response.getData().stream().map(BranchInfo::toMap).toList()
                        : null;

                Map<String, Object> payload = Map.of("branches", branches);
                args.reply(TypeDTTP.BRANCH_GET_ALL.getValue(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.BRANCH_GET_ALL.getValue(), e);
            }
        });

        // ---------------- CREATE ----------------
        server.on(TypeDTTP.BRANCH_CREATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.BRANCH_CREATE.getValue())) return;

                BranchRequestDTO request = new BranchRequestDTO(args.data);
                ResponseDTO<BranchInfo> response = controller.createBranch(request);

                args.reply(TypeDTTP.BRANCH_CREATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.BRANCH_CREATE.getValue(), e);
            }
        });

        // ---------------- UPDATE ----------------
        server.on(TypeDTTP.BRANCH_UPDATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.BRANCH_UPDATE.getValue())) return;

                BranchRequestDTO request = new BranchRequestDTO(args.data);
                ResponseDTO<BranchInfo> response = controller.updateBranch(request);

                args.reply(TypeDTTP.BRANCH_UPDATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.BRANCH_UPDATE.getValue(), e);
            }
        });

        // ---------------- DELETE ----------------
        server.on(TypeDTTP.BRANCH_DELETE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.BRANCH_DELETE.getValue())) return;

                BranchRequestDTO request = new BranchRequestDTO(args.data);
                ResponseDTO<BranchInfo> response = controller.deleteBranch(request);

                args.reply(TypeDTTP.BRANCH_DELETE.getValue(), null, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.BRANCH_DELETE.getValue(), e);
            }
        });
    }
}
