package edu.ptithcm.routes.shiftassignment;

import edu.ptithcm.controller.ShiftAssignmentController;
import edu.ptithcm.dto.request.shiftassignment.ShiftAssignmentRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.ShiftAssignmentInfo;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.utils.ReplyUtils;

import java.util.Map;
import java.util.List;

import edu.ptithcm.configs.Role;
import edu.ptithcm.configs.TypeDTTP;

public class ShiftAssignmentRoute {
    private final DTTP server;
    private final DTTPStateManager manager;
    private final ShiftAssignmentController controller;

    public ShiftAssignmentRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
        this.controller = new ShiftAssignmentController();
    }

    public void register() {
        // ---------------- GET ALL ----------------
        server.on(TypeDTTP.SHIFT_ASSIGNMENT_GET_ALL.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(),
                        args, TypeDTTP.SHIFT_ASSIGNMENT_GET_ALL.getValue()))
                    return;

                ResponseDTO<List<ShiftAssignmentInfo>> response = controller.getAllAssignments();
                List<Map<String, Object>> data = response.getData() != null
                        ? response.getData().stream().map(ShiftAssignmentInfo::toMap).toList()
                        : null;

                args.reply(TypeDTTP.SHIFT_ASSIGNMENT_GET_ALL.getValue(),
                        Map.of("assignments", data),
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.SHIFT_ASSIGNMENT_GET_ALL.getValue(), e);
            }
        });

        // ---------------- CREATE ----------------
        server.on(TypeDTTP.SHIFT_ASSIGNMENT_CREATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.SHIFT_ASSIGNMENT_CREATE.getValue()))
                    return;

                ShiftAssignmentRequestDTO request = new ShiftAssignmentRequestDTO(args.data);
                ResponseDTO<ShiftAssignmentInfo> response = controller.createAssignment(request);

                args.reply(TypeDTTP.SHIFT_ASSIGNMENT_CREATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.SHIFT_ASSIGNMENT_CREATE.getValue(), e);
            }
        });

        // ---------------- DELETE ----------------
        server.on(TypeDTTP.SHIFT_ASSIGNMENT_DELETE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.SHIFT_ASSIGNMENT_DELETE.getValue()))
                    return;

                ShiftAssignmentRequestDTO request = new ShiftAssignmentRequestDTO(args.data);
                ResponseDTO<ShiftAssignmentInfo> response = controller.deleteAssignment(request);

                args.reply(TypeDTTP.SHIFT_ASSIGNMENT_DELETE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.SHIFT_ASSIGNMENT_DELETE.getValue(), e);
            }
        });

        // ---------------- GET BY SHIFT ----------------
        server.on(TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_SHIFT.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_SHIFT.getValue()))
                    return;

                Integer shiftId = args.data.get("shiftId") instanceof Number ? ((Number) args.data.get("shiftId")).intValue() : null;
                ResponseDTO<List<ShiftAssignmentInfo>> response = controller.getByShiftId(shiftId);

                List<Map<String,Object>> data = response.getData() != null
                        ? response.getData().stream().map(ShiftAssignmentInfo::toMap).toList()
                        : null;

                args.reply(TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_SHIFT.getValue(),
                        Map.of("assignments", data),
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_SHIFT.getValue(), e);
            }
        });

            // ---------------- GET BY EMPLOYEE ----------------
            server.on(TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_EMPLOYEE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_EMPLOYEE.getValue()))
                return;

                String employeeId = args.data.get("employeeId") != null ? args.data.get("employeeId").toString() : null;
                ResponseDTO<List<ShiftAssignmentInfo>> response = controller.getByEmployeeId(employeeId);

                List<Map<String,Object>> data = response.getData() != null
                        ? response.getData().stream().map(ShiftAssignmentInfo::toMap).toList()
                        : null;

                args.reply(TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_EMPLOYEE.getValue(),
                        Map.of("assignments", data),
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_EMPLOYEE.getValue(), e);
            }
        });

        // ---------------- GET BY BRANCH ----------------
        server.on(TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_BRANCH.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_BRANCH.getValue()))
                    return;

                Integer branchId = args.data.get("branchId") instanceof Number ? ((Number) args.data.get("branchId")).intValue() : null;
                ResponseDTO<List<ShiftAssignmentInfo>> response = controller.getByBranchId(branchId);

                List<Map<String,Object>> data = response.getData() != null
                        ? response.getData().stream().map(ShiftAssignmentInfo::toMap).toList()
                        : null;

                args.reply(TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_BRANCH.getValue(),
                        Map.of("assignments", data),
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.SHIFT_ASSIGNMENT_GET_BY_BRANCH.getValue(), e);
            }
        });
    }
}
