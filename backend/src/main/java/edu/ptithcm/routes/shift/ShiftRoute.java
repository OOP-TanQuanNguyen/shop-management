package edu.ptithcm.routes.shift;

import java.util.List;
import java.util.Map;
import edu.ptithcm.configs.Role;
import edu.ptithcm.configs.TypeDTTP;
import edu.ptithcm.controller.ShiftController;
import edu.ptithcm.dto.request.shift.ShiftRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.ShiftInfo;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.utils.ReplyUtils;

public class ShiftRoute {

    private final DTTP server;
    private final DTTPStateManager manager;
    private final ShiftController controller;

    public ShiftRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
        this.controller = new ShiftController();
    }

    public void register() {
        // ---------------- GET ALL ----------------
        server.on(TypeDTTP.SHIFT_GET_ALL.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.SHIFT_GET_ALL.getValue())) return;

                ResponseDTO<List<ShiftInfo>> response = controller.getAllShifts();
                List<Map<String, Object>> shifts = null;
                if (response.getData() != null) {
                    shifts = response.getData().stream().map(ShiftInfo::toMap).toList();
                }

                Map<String, Object> payload = Map.of("shifts", shifts);
                args.reply(TypeDTTP.SHIFT_GET_ALL.getValue(), payload, response.getStatus(), response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.SHIFT_GET_ALL.getValue(), e);
            }
        });

        // ---------------- CREATE ----------------
        server.on(TypeDTTP.SHIFT_CREATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.SHIFT_CREATE.getValue())) return;

                ShiftRequestDTO request = new ShiftRequestDTO(args.data);
                ResponseDTO<ShiftInfo> response = controller.createShift(request);

                args.reply(TypeDTTP.SHIFT_CREATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.SHIFT_CREATE.getValue(), e);
            }
        });

        // ---------------- UPDATE ----------------
        server.on(TypeDTTP.SHIFT_UPDATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.SHIFT_UPDATE.getValue())) return;

                ShiftRequestDTO request = new ShiftRequestDTO(args.data);
                ResponseDTO<ShiftInfo> response = controller.updateShift(request);

                args.reply(TypeDTTP.SHIFT_UPDATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.SHIFT_UPDATE.getValue(), e);
            }
        });

        // ---------------- DELETE ----------------
        server.on(TypeDTTP.SHIFT_DELETE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.SHIFT_DELETE.getValue())) return;

                ShiftRequestDTO request = new ShiftRequestDTO(args.data);
                ResponseDTO<ShiftInfo> response = controller.deleteShift(request);

                args.reply(TypeDTTP.SHIFT_DELETE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.SHIFT_DELETE.getValue(), e);
            }
        });

        // ---------------- GET BY ID ----------------
        server.on(TypeDTTP.SHIFT_GET_BY_ID.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.SHIFT_GET_BY_ID.getValue())) return;

                ShiftRequestDTO request = new ShiftRequestDTO(args.data);
                ResponseDTO<ShiftInfo> response = controller.getShiftsById(request);

                args.reply(TypeDTTP.SHIFT_GET_BY_ID.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.SHIFT_GET_BY_ID.getValue(), e);
            }
        });
    }
}
