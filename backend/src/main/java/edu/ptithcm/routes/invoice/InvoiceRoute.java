package edu.ptithcm.routes.invoice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ptithcm.configs.Role;
import edu.ptithcm.configs.TypeDTTP;
import edu.ptithcm.controller.InvoiceController;
import edu.ptithcm.dto.request.invoice.InvoiceRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.InvoiceInfo;
import edu.ptithcm.middleware.AuthenMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.utils.ReplyUtils;

public class InvoiceRoute {

    private final DTTP server;
    private final DTTPStateManager manager;
    private final InvoiceController controller;

    public InvoiceRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
        this.controller = new InvoiceController();
    }

    public void register() {

        // ---------------- GET ALL ----------------
        server.on(TypeDTTP.INVOICE_GET_ALL.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.INVOICE_GET_ALL.getValue())) return;

                ResponseDTO<List<InvoiceInfo>> response = controller.getAll();
                List<Map<String, Object>> invoices = response.getData() != null
                        ? response.getData().stream().map(InvoiceInfo::toMap).collect(Collectors.toList())
                        : null;

                Map<String, Object> payload = Map.of("invoices", invoices);
                args.reply(TypeDTTP.INVOICE_GET_ALL.getValue(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_GET_ALL.getValue(), e);
            }
        });

        // ---------------- CREATE ----------------
        server.on(TypeDTTP.INVOICE_CREATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.INVOICE_CREATE.getValue())) return;

                InvoiceRequestDTO request = new InvoiceRequestDTO((Map<String, Object>) args.data);
                ResponseDTO<InvoiceInfo> response = controller.create(request);

                args.reply(TypeDTTP.INVOICE_CREATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_CREATE.getValue(), e);
            }
        });

        // ---------------- UPDATE ----------------
        server.on(TypeDTTP.INVOICE_UPDATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.INVOICE_UPDATE.getValue())) return;

                InvoiceRequestDTO request = new InvoiceRequestDTO((Map<String, Object>) args.data);
                ResponseDTO<InvoiceInfo> response = controller.update(request);

                args.reply(TypeDTTP.INVOICE_UPDATE.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_UPDATE.getValue(), e);
            }
        });

        // ---------------- DELETE ----------------
        server.on(TypeDTTP.INVOICE_DELETE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.INVOICE_DELETE.getValue())) return;

                Map<String, Object> data = (Map<String, Object>) args.data;
                String invoiceId = (String) data.get("invoiceId");

                ResponseDTO<InvoiceInfo> response = controller.delete(invoiceId);
                args.reply(TypeDTTP.INVOICE_DELETE.getValue(), null, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_DELETE.getValue(), e);
            }
        });

        // ---------------- GET BY CUSTOMER ----------------
        server.on(TypeDTTP.INVOICE_GET_BY_CUSTOMER.getValue(), args -> {
            try {
                Map<String, Object> data = (Map<String, Object>) args.data;
                String customerId = (String) data.get("customerId");

                ResponseDTO<List<InvoiceInfo>> response = controller.getByCustomer(customerId);
                List<Map<String, Object>> invoices = response.getData() != null
                        ? response.getData().stream().map(InvoiceInfo::toMap).collect(Collectors.toList())
                        : null;

                Map<String, Object> payload = Map.of("invoices", invoices);
                args.reply(TypeDTTP.INVOICE_GET_BY_CUSTOMER.getValue(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_GET_BY_CUSTOMER.getValue(), e);
            }
        });

        // ---------------- GET BY BRANCH ----------------
        server.on(TypeDTTP.INVOICE_GET_BY_BRANCH.getValue(), args -> {
            try {
                Map<String, Object> data = (Map<String, Object>) args.data;
                Integer branchId = ((Number) data.get("branchId")).intValue();

                ResponseDTO<List<InvoiceInfo>> response = controller.getByBranch(branchId);
                List<Map<String, Object>> invoices = response.getData() != null
                        ? response.getData().stream().map(InvoiceInfo::toMap).collect(Collectors.toList())
                        : null;

                Map<String, Object> payload = Map.of("invoices", invoices);
                args.reply(TypeDTTP.INVOICE_GET_BY_BRANCH.getValue(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_GET_BY_BRANCH.getValue(), e);
            }
        });

        // ---------------- GET BY EMPLOYEE ----------------
        server.on(TypeDTTP.INVOICE_GET_BY_EMPLOYEE.getValue(), args -> {
            try {
                Map<String, Object> data = (Map<String, Object>) args.data;
                String employeeId = (String) data.get("employeeId");

                ResponseDTO<List<InvoiceInfo>> response = controller.getByEmployee(employeeId);
                List<Map<String, Object>> invoices = response.getData() != null
                        ? response.getData().stream().map(InvoiceInfo::toMap).collect(Collectors.toList())
                        : null;

                Map<String, Object> payload = Map.of("invoices", invoices);
                args.reply(TypeDTTP.INVOICE_GET_BY_EMPLOYEE.getValue(), payload, response.getStatus(), response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_GET_BY_EMPLOYEE.getValue(), e);
            }
        });
    }
}
