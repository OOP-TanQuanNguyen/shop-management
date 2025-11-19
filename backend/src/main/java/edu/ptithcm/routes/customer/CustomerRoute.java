package edu.ptithcm.routes.customer;

import edu.ptithcm.configs.Role;
import edu.ptithcm.controller.CustomerController;
import edu.ptithcm.dto.request.customer.CustomerRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.CustomerInfo;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.utils.ReplyUtils;

import java.util.List;
import java.util.Map;

public class CustomerRoute {
    private final DTTP server;
    private final DTTPStateManager manager;
    private final CustomerController controller;

    public CustomerRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
        this.controller = new CustomerController();
    }

    public void register() {
        server.on("CUSTOMER_GET_ALL", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, "CUSTOMER_GET_ALL")) return;

                ResponseDTO<List<CustomerInfo>> response = controller.getAllCustomers();
                List<Map<String,Object>> customers = response.getData() != null ?
                        response.getData().stream().map(CustomerInfo::toMap).toList() : null;

                args.reply("CUSTOMER_GET_ALL", Map.of("customers", customers), response.getStatus(), response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, "CUSTOMER_GET_ALL", e);
            }
        });

        server.on("CUSTOMER_CREATE", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, "CUSTOMER_CREATE")) return;

                String sessionId = args.data.get("sessionId") != null ? args.data.get("sessionId").toString() : null;
                CustomerRequestDTO request = new CustomerRequestDTO(args.data);
                var response = controller.createCustomer(request, sessionId);
                args.reply("CUSTOMER_CREATE",
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, "CUSTOMER_CREATE", e);
            }
        });

        server.on("CUSTOMER_UPDATE", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, "CUSTOMER_UPDATE")) return;

                String sessionId = args.data.get("sessionId") != null ? args.data.get("sessionId").toString() : null;
                CustomerRequestDTO request = new CustomerRequestDTO(args.data);
                var response = controller.updateCustomer(request, sessionId);
                args.reply("CUSTOMER_UPDATE",
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, "CUSTOMER_UPDATE", e);
            }
        });

        server.on("CUSTOMER_DELETE", args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, "CUSTOMER_DELETE")) return;

                CustomerRequestDTO request = new CustomerRequestDTO(args.data);
                var response = controller.deleteCustomer(request);
                args.reply("CUSTOMER_DELETE",
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, "CUSTOMER_DELETE", e);
            }
        });

        server.on("CUSTOMER_GET_BY_ID", args -> {
            try {
                CustomerRequestDTO request = new CustomerRequestDTO(args.data);
                var response = controller.getCustomerById(request);
                args.reply("CUSTOMER_GET_BY_ID",
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, "CUSTOMER_GET_BY_ID", e);
            }
        });

        server.on("CUSTOMER_GET_BY_PHONE", args -> {
            try {
                String phone = args.data.get("phone") != null ? args.data.get("phone").toString() : null;
                var response = controller.getCustomerByPhone(phone);
                args.reply("CUSTOMER_GET_BY_PHONE",
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());
            } catch (Exception e) {
                ReplyUtils.replyError(args, "CUSTOMER_GET_BY_PHONE", e);
            }
        });
    }
}
