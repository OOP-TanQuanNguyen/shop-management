package edu.ptithcm.routes.inventory;

import edu.ptithcm.configs.Role;
import edu.ptithcm.configs.TypeDTTP;
import edu.ptithcm.controller.InventoryController;
import edu.ptithcm.dto.request.inventory.InventoryRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.InventoryInfo;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.utils.ReplyUtils;
import edu.ptithcm.utils.RequestUtil;
import java.util.List;
import java.util.Map;

public class InventoryRoute {

    private final DTTP server;
    private final DTTPStateManager manager;
    private final InventoryController controller;

    public InventoryRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
        this.controller = new InventoryController();
    }

    public void register() {
        server.on(TypeDTTP.INVENTORY_GET_ALL.getValue(), args -> {
            try {
                ResponseDTO<List<InventoryInfo>> response =
                    controller.getAllInventories();

                System.out.println(
                    "GetAllInventory route (response): " + response.toString()
                );
                List<Map<String, Object>> inventories = response.getData() !=
                    null
                    ? response
                          .getData()
                          .stream()
                          .map(InventoryInfo::toMap)
                          .toList()
                    : null;

                System.out.println(
                    "GetAllInventory route : " + inventories.toString()
                );

                args.reply(
                    TypeDTTP.INVENTORY_GET_ALL.getValue(),
                    Map.of("inventories", inventories),
                    response.getStatus(),
                    response.getMessage()
                );
            } catch (Exception e) {
                ReplyUtils.replyError(
                    args,
                    TypeDTTP.INVENTORY_GET_ALL.getValue(),
                    e
                );
            }
        });

        server.on(TypeDTTP.INVENTORY_CREATE.getValue(), args -> {
            try {
                if (
                    !AuthenMiddleWare.hasPermission(
                        manager,
                        server,
                        Role.ADMIN.getValue(),
                        args,
                        TypeDTTP.INVENTORY_CREATE.getValue()
                    )
                ) {
                    return;
                }

                InventoryRequestDTO request = new InventoryRequestDTO(
                    args.data
                );
                ResponseDTO<InventoryInfo> response =
                    controller.createInventory(request);

                args.reply(
                    TypeDTTP.INVENTORY_CREATE.getValue(),
                    response.getData() != null
                        ? response.getData().toMap()
                        : null,
                    response.getStatus(),
                    response.getMessage()
                );
            } catch (Exception e) {
                ReplyUtils.replyError(
                    args,
                    TypeDTTP.INVENTORY_CREATE.getValue(),
                    e
                );
            }
        });

        /* ============================================================
           UPDATE
        ============================================================ */
        server.on(TypeDTTP.INVENTORY_UPDATE.getValue(), args -> {
            try {
                if (
                    !AuthenMiddleWare.hasPermission(
                        manager,
                        server,
                        Role.ADMIN.getValue(),
                        args,
                        TypeDTTP.INVENTORY_UPDATE.getValue()
                    )
                ) {
                    return;
                }

                InventoryRequestDTO request = new InventoryRequestDTO(
                    args.data
                );
                ResponseDTO<InventoryInfo> response =
                    controller.updateInventory(request);

                args.reply(
                    TypeDTTP.INVENTORY_UPDATE.getValue(),
                    response.getData() != null
                        ? response.getData().toMap()
                        : null,
                    response.getStatus(),
                    response.getMessage()
                );
            } catch (Exception e) {
                ReplyUtils.replyError(
                    args,
                    TypeDTTP.INVENTORY_UPDATE.getValue(),
                    e
                );
            }
        });

        /* ============================================================
           DELETE
        ============================================================ */
        server.on(TypeDTTP.INVENTORY_DELETE.getValue(), args -> {
            try {
                if (
                    !AuthenMiddleWare.hasPermission(
                        manager,
                        server,
                        Role.ADMIN.getValue(),
                        args,
                        TypeDTTP.INVENTORY_DELETE.getValue()
                    )
                ) {
                    return;
                }

                InventoryRequestDTO request = new InventoryRequestDTO(
                    args.data
                );
                ResponseDTO<InventoryInfo> response =
                    controller.deleteInventory(request);

                args.reply(
                    TypeDTTP.INVENTORY_DELETE.getValue(),
                    response.getData() != null
                        ? response.getData().toMap()
                        : null,
                    response.getStatus(),
                    response.getMessage()
                );
            } catch (Exception e) {
                ReplyUtils.replyError(
                    args,
                    TypeDTTP.INVENTORY_DELETE.getValue(),
                    e
                );
            }
        });

        server.on(TypeDTTP.INVENTORY_GET_BY_BRANCH.getValue(), args -> {
            try {
                // Parse branchId  → xử lý được: 3, "3", "3.0", 3.0
                Integer branchId = RequestUtil.toInt(args.data.get("branchId"));

                ResponseDTO<List<InventoryInfo>> response =
                    controller.getInventoriesByBranch(branchId);

                List<Map<String, Object>> inventories = response.getData() !=
                    null
                    ? response
                          .getData()
                          .stream()
                          .map(InventoryInfo::toMap)
                          .toList()
                    : null;

                args.reply(
                    TypeDTTP.INVENTORY_GET_BY_BRANCH.getValue(),
                    Map.of("inventories", inventories),
                    response.getStatus(),
                    response.getMessage()
                );
            } catch (Exception e) {
                ReplyUtils.replyError(
                    args,
                    TypeDTTP.INVENTORY_GET_BY_BRANCH.getValue(),
                    e
                );
            }
        });
    }
}
