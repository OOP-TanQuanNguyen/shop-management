package edu.ptithcm.routes.loyalty;

import java.util.List;
import java.util.Map;

import edu.ptithcm.configs.Role;
import edu.ptithcm.configs.TypeDTTP;
import edu.ptithcm.controller.LoyaltyController;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.LoyaltyInfo;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.utils.ReplyUtils;
import edu.ptithcm.utils.RequestUtil;

public class LoyaltyRoute {

    private final DTTP server;
    private final DTTPStateManager manager;
    private final LoyaltyController controller;

    public LoyaltyRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
        this.controller = new LoyaltyController();
    }

    public void register() {

        // GET ALL
        server.on(TypeDTTP.LOYALTY_GET_ALL.getValue(), args -> {
            try {
                ResponseDTO<List<LoyaltyInfo>> response = controller.getAllLoyalty();
                List<Map<String, Object>> loyalties = null;

                if (response.getData() != null) {
                    loyalties = response.getData()
                            .stream()
                            .map(LoyaltyInfo::toMap)
                            .toList();
                }

                Map<String, Object> payload = Map.of("loyalties", loyalties);

                args.reply(
                        TypeDTTP.LOYALTY_GET_ALL.getValue(),
                        payload,
                        response.getStatus(),
                        response.getMessage()
                );

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.LOYALTY_GET_ALL.getValue(), e);
            }
        });

        // GET BY CUSTOMER
        server.on(TypeDTTP.LOYALTY_GET_BY_CUSTOMER.getValue(), args -> {
            try {
                String customerId = (String) args.data.get("customerId");

                ResponseDTO<LoyaltyInfo> response
                        = controller.getLoyaltyByCustomer(customerId);

                args.reply(
                        TypeDTTP.LOYALTY_GET_BY_CUSTOMER.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage()
                );

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.LOYALTY_GET_BY_CUSTOMER.getValue(), e);
            }
        });

        // CREATE
        server.on(TypeDTTP.LOYALTY_CREATE.getValue(), args -> {
            try {
                String customerId = (String) args.data.get("customerId");

                ResponseDTO<LoyaltyInfo> response
                        = controller.createLoyalty(customerId);

                args.reply(
                        TypeDTTP.LOYALTY_CREATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage()
                );

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.LOYALTY_CREATE.getValue(), e);
            }
        });

        // UPDATE
        server.on(TypeDTTP.LOYALTY_UPDATE.getValue(), args -> {
            try {
                String customerId = (String) args.data.get("customerId");

                // FIX TẠI ĐÂY: DÙNG RequestUtil
                int pointsChange = RequestUtil.toInt(args.data.get("pointsChange"), 0);

                ResponseDTO<LoyaltyInfo> response
                        = controller.updateLoyalty(customerId, pointsChange);

                args.reply(
                        TypeDTTP.LOYALTY_UPDATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage()
                );

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.LOYALTY_UPDATE.getValue(), e);
            }
        });

        // DELETE
        server.on(TypeDTTP.LOYALTY_DELETE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(
                        manager, server, Role.ADMIN.getValue(),
                        args, TypeDTTP.LOYALTY_DELETE.getValue()
                )) {
                    return;
                }

                String customerId = (String) args.data.get("customerId");

                ResponseDTO<LoyaltyInfo> response
                        = controller.deleteLoyalty(customerId);

                args.reply(
                        TypeDTTP.LOYALTY_DELETE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage()
                );

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.LOYALTY_DELETE.getValue(), e);
            }
        });
    }
}
