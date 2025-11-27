package edu.ptithcm.services.pos;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.ptithcm.app.actions.InvoiceAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;

public class InvoiceService {

    private static final Logger logger = Logger.getLogger(InvoiceService.class.getName());

    private static final String SUCCESS = "SUCCESS";
    private static final String INVALID = "INVALID";
    private static final String ERROR = "ERROR";
    private static final String REQUEST = "REQUEST";

    private static final String INVOICES_KEY = "invoices";
    private static final int RELOAD_DELAY = 150;

    private final DTTP client;
    private final Store store = Store.getInstance();

    public InvoiceService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    /* ======================================================
       REGISTER RESPONSE HANDLERS
    ====================================================== */
    private void registerHandlers() {

        client.on("INVOICE_GET_ALL", args -> handleListResponse(args, InvoiceAction.INVOICE_GET_ALL));
        client.on("INVOICE_GET_BY_CUSTOMER", args -> handleListResponse(args, InvoiceAction.INVOICE_GET_BY_CUSTOMER));
        client.on("INVOICE_GET_BY_BRANCH", args -> handleListResponse(args, InvoiceAction.INVOICE_GET_BY_BRANCH));
        client.on("INVOICE_GET_BY_EMPLOYEE", args -> handleListResponse(args, InvoiceAction.INVOICE_GET_BY_EMPLOYEE));

        client.on("INVOICE_CREATE", this::handleCreate);
        client.on("INVOICE_UPDATE", this::handleUpdate);
        client.on("INVOICE_DELETE", this::handleDelete);

        client.on("INVOICE_CONFIRM", this::handleConfirm);
        client.on("INVOICE_CANCEL", this::handleCancel);
    }

    /* ======================================================
       HANDLE LIST RESPONSES
    ====================================================== */
    @SuppressWarnings("unchecked")
    private void handleListResponse(DTTP.DTTPArgs args, InvoiceAction action) {

        logger.info(action + " response: " + args.status);

        if (SUCCESS.equals(args.status)) {

            List<Map<String, Object>> list
                    = (List<Map<String, Object>>) args.data.get(INVOICES_KEY);

            store.dispatch(action.toString(), list);

        } else {
            store.dispatch(InvoiceAction.INVOICE_ERROR.toString(), args.message);
        }
    }

    /* ======================================================
       CREATE (DRAFT)
    ====================================================== */
    private void handleCreate(DTTP.DTTPArgs args) {
        logger.info("INVOICE_CREATE response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            store.dispatch(InvoiceAction.INVOICE_CREATE.toString(), args.data);
            store.dispatch(InvoiceAction.INVOICE_MESSAGE.toString(), args.message);
        } else {
            store.dispatch(InvoiceAction.INVOICE_ERROR.toString(), args.message);
        }
    }

    /* ======================================================
       UPDATE (DRAFT)
    ====================================================== */
    private void handleUpdate(DTTP.DTTPArgs args) {
        logger.info("INVOICE_UPDATE response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            store.dispatch(InvoiceAction.INVOICE_UPDATE.toString(), args.data);
            store.dispatch(InvoiceAction.INVOICE_MESSAGE.toString(), args.message);
        } else {
            store.dispatch(InvoiceAction.INVOICE_ERROR.toString(), args.message);
        }
    }

    /* ======================================================
       DELETE
    ====================================================== */
    private void handleDelete(DTTP.DTTPArgs args) {
        logger.info("INVOICE_DELETE response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            store.dispatch(InvoiceAction.INVOICE_DELETE.toString(), null);
            store.dispatch(InvoiceAction.INVOICE_MESSAGE.toString(), args.message);
            reloadAll();
        } else {
            store.dispatch(InvoiceAction.INVOICE_ERROR.toString(), args.message);
        }
    }

    /* ======================================================
       CONFIRM
    ====================================================== */
    private void handleConfirm(DTTP.DTTPArgs args) {
        logger.info("INVOICE_CONFIRM response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            store.dispatch(InvoiceAction.INVOICE_CONFIRM.toString(), args.data);
            store.dispatch(InvoiceAction.INVOICE_MESSAGE.toString(), args.message);
            reloadAll();
        } else {
            store.dispatch(InvoiceAction.INVOICE_ERROR.toString(), args.message);
        }
    }

    /* ======================================================
       CANCEL
    ====================================================== */
    private void handleCancel(DTTP.DTTPArgs args) {
        logger.info("INVOICE_CANCEL response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            store.dispatch(InvoiceAction.INVOICE_CANCEL.toString(), args.data);
            store.dispatch(InvoiceAction.INVOICE_MESSAGE.toString(), args.message);
            reloadAll();
        } else {
            store.dispatch(InvoiceAction.INVOICE_ERROR.toString(), args.message);
        }
    }

    /* ======================================================
       AUTO RELOAD LIST
    ====================================================== */
    private void reloadAll() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    getInvoicesForEmployee();
                } catch (IOException e) {
                    store.dispatch(InvoiceAction.INVOICE_ERROR.toString(),
                            "Không thể tải lại danh sách hóa đơn");
                }
            }
        }, RELOAD_DELAY);
    }

    private void ensureClient() throws IOException {
        if (client == null) {
            throw new IOException("DTTP client null");
        }
    }

    /* ======================================================
       PUBLIC API
    ====================================================== */
    public void getInvoicesForEmployee() throws IOException {
        ensureClient();
        logger.info("Sending INVOICE_GET_BY_EMPLOYEE");
        client.send("INVOICE_GET_BY_EMPLOYEE", Map.of(), REQUEST, "Lấy hóa đơn nhân viên");
    }

    public void getInvoicesByCustomer(String customerId) throws IOException {
        ensureClient();
        client.send("INVOICE_GET_BY_CUSTOMER", Map.of("customerId", customerId), REQUEST, "Lấy hóa đơn theo khách");
    }

    public void getInvoicesByBranch(Integer branchId) throws IOException {
        ensureClient();
        client.send("INVOICE_GET_BY_BRANCH", Map.of("branchId", branchId), REQUEST, "Lấy hóa đơn theo chi nhánh");
    }

    public void createInvoice(Map<String, Object> payload) throws IOException {
        ensureClient();
        client.send("INVOICE_CREATE", payload, REQUEST, "Tạo hóa đơn (draft)");
    }

    public void updateInvoice(Map<String, Object> payload) throws IOException {
        ensureClient();
        client.send("INVOICE_UPDATE", payload, REQUEST, "Cập nhật hóa đơn draft");
    }

    public void confirmInvoice(String invoiceId) throws IOException {
        ensureClient();
        client.send("INVOICE_CONFIRM", Map.of("invoiceId", invoiceId), REQUEST, "Xác nhận hóa đơn");
    }

    public void cancelInvoice(String invoiceId) throws IOException {
        ensureClient();
        client.send("INVOICE_CANCEL", Map.of("invoiceId", invoiceId), REQUEST, "Hủy hóa đơn");
    }

    public void deleteInvoice(String invoiceId) throws IOException {
        ensureClient();
        client.send("INVOICE_DELETE", Map.of("invoiceId", invoiceId), REQUEST, "Xóa hóa đơn");
    }
}
