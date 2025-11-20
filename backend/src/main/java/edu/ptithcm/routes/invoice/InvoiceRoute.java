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

                ResponseDTO<List<InvoiceInfo>> response = controller.getAllInvoices();
                List<Map<String, Object>> invoices = response.getData() != null
                        ? response.getData().stream().map(InvoiceInfo::toMap).collect(Collectors.toList())
                        : null;

                args.reply(TypeDTTP.INVOICE_GET_ALL.getValue(),
                        Map.of("invoices", invoices),
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_GET_ALL.getValue(), e);
            }
        });

        // ---------------- CREATE ----------------
        server.on(TypeDTTP.INVOICE_CREATE.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.INVOICE_CREATE.getValue())) return;

                InvoiceRequestDTO request = new InvoiceRequestDTO((Map<String, Object>) args.data);
                ResponseDTO<InvoiceInfo> response = controller.createInvoice(request);

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
                ResponseDTO<InvoiceInfo> response = controller.updateInvoice(request);

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

                InvoiceRequestDTO request = new InvoiceRequestDTO((Map<String, Object>) args.data);
                ResponseDTO<InvoiceInfo> response = controller.deleteInvoice(request);

                args.reply(TypeDTTP.INVOICE_DELETE.getValue(),
                        null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_DELETE.getValue(), e);
            }
        });

        // ---------------- GET BY CUSTOMER ----------------
        server.on(TypeDTTP.INVOICE_GET_BY_CUSTOMER.getValue(), args -> {
            try {
                InvoiceRequestDTO request = new InvoiceRequestDTO((Map<String, Object>) args.data);
                ResponseDTO<List<InvoiceInfo>> response = controller.getInvoiceByCustomer(request);

                List<Map<String, Object>> invoices = response.getData() != null
                        ? response.getData().stream().map(InvoiceInfo::toMap).collect(Collectors.toList())
                        : null;

                args.reply(TypeDTTP.INVOICE_GET_BY_CUSTOMER.getValue(),
                        Map.of("invoices", invoices),
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_GET_BY_CUSTOMER.getValue(), e);
            }
        });

        // ---------------- GET BY BRANCH ----------------
        server.on(TypeDTTP.INVOICE_GET_BY_BRANCH.getValue(), args -> {
            try {
                InvoiceRequestDTO request = new InvoiceRequestDTO((Map<String, Object>) args.data);
                ResponseDTO<List<InvoiceInfo>> response = controller.getInvoiceByBranch(request);

                List<Map<String, Object>> invoices = response.getData() != null
                        ? response.getData().stream().map(InvoiceInfo::toMap).collect(Collectors.toList())
                        : null;

                args.reply(TypeDTTP.INVOICE_GET_BY_BRANCH.getValue(),
                        Map.of("invoices", invoices),
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_GET_BY_BRANCH.getValue(), e);
            }
        });

        // ---------------- GET BY EMPLOYEE ----------------
        server.on(TypeDTTP.INVOICE_GET_BY_EMPLOYEE.getValue(), args -> {
            try {
                InvoiceRequestDTO request = new InvoiceRequestDTO((Map<String, Object>) args.data);
                ResponseDTO<List<InvoiceInfo>> response = controller.getInvoiceByEmployee(request);

                List<Map<String, Object>> invoices = response.getData() != null
                        ? response.getData().stream().map(InvoiceInfo::toMap).collect(Collectors.toList())
                        : null;

                args.reply(TypeDTTP.INVOICE_GET_BY_EMPLOYEE.getValue(),
                        Map.of("invoices", invoices),
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_GET_BY_EMPLOYEE.getValue(), e);
            }
        });

        // ---------------- CONFIRM INVOICE ----------------
        server.on(TypeDTTP.INVOICE_CONFIRM.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.INVOICE_CONFIRM.getValue()))
                    return;

                InvoiceRequestDTO request = new InvoiceRequestDTO((Map<String, Object>) args.data);
                ResponseDTO<InvoiceInfo> response = controller.confirmInvoice(request);

                args.reply(TypeDTTP.INVOICE_CONFIRM.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_CONFIRM.getValue(), e);
            }
        });

        // ---------------- CANCEL INVOICE ----------------
        server.on(TypeDTTP.INVOICE_CANCEL.getValue(), args -> {
            try {
                if (!AuthenMiddleWare.hasPermission(manager, server, Role.ADMIN.getValue(), args, TypeDTTP.INVOICE_CANCEL.getValue()))
                    return;

                InvoiceRequestDTO request = new InvoiceRequestDTO((Map<String, Object>) args.data);
                ResponseDTO<InvoiceInfo> response = controller.cancelInvoice(request);

                args.reply(TypeDTTP.INVOICE_CANCEL.getValue(),
                        response.getData() != null ? response.getData().toMap() : null,
                        response.getStatus(),
                        response.getMessage());

            } catch (Exception e) {
                ReplyUtils.replyError(args, TypeDTTP.INVOICE_CANCEL.getValue(), e);
            }
        });
    }
}
