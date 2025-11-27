package edu.ptithcm.app.reducers;

import edu.ptithcm.app.actions.InvoiceAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.InvoiceInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvoiceReducer {

    private InvoiceReducer() {
    }

    @SuppressWarnings("unchecked")
    public static void register(Store store) {

        // ====================================================
        // LOAD LIST
        // ====================================================
        store.registerReducer(InvoiceAction.INVOICE_GET_ALL.toString(), payload -> {
            updateInvoiceList(store, payload);
        });

        store.registerReducer(InvoiceAction.INVOICE_GET_BY_CUSTOMER.toString(), payload -> {
            updateInvoiceList(store, payload);
        });

        store.registerReducer(InvoiceAction.INVOICE_GET_BY_BRANCH.toString(), payload -> {
            updateInvoiceList(store, payload);
        });

        store.registerReducer(InvoiceAction.INVOICE_GET_BY_EMPLOYEE.toString(), payload -> {
            updateInvoiceList(store, payload);
        });

        // ====================================================
        // CREATE DRAFT
        // ====================================================
        store.registerReducer(InvoiceAction.INVOICE_CREATE.toString(), payload -> {
            if (payload instanceof Map<?, ?> map) {
                InvoiceInfo info = InvoiceInfo.fromMap((Map<String, Object>) map);
                store.getAppState().set("Invoice", info);
            }
        });

        // ====================================================
        // UPDATE DRAFT
        // ====================================================
        store.registerReducer(InvoiceAction.INVOICE_UPDATE.toString(), payload -> {
            if (payload instanceof Map<?, ?> map) {
                InvoiceInfo info = InvoiceInfo.fromMap((Map<String, Object>) map);
                store.getAppState().set("Invoice", info);
            }
        });

        // ====================================================
        // CONFIRM
        // ====================================================
        store.registerReducer(InvoiceAction.INVOICE_CONFIRM.toString(), payload -> {
            store.getAppState().set("Invoice", null);
        });

        // ====================================================
        // CANCEL
        // ====================================================
        store.registerReducer(InvoiceAction.INVOICE_CANCEL.toString(), payload -> {
            store.getAppState().set("Invoice", null);
        });

        // ====================================================
        // DELETE
        // ====================================================
        store.registerReducer(InvoiceAction.INVOICE_DELETE.toString(), payload -> {
            store.getAppState().set("Invoice", null);
        });

        // ====================================================
        // ✅ FIX: THÊM INVOICE_MESSAGE HANDLER
        // ====================================================
        store.registerReducer(InvoiceAction.INVOICE_MESSAGE.toString(), payload -> {
            if (payload != null) {
                store.getAppState().set("InvoiceMessage", payload.toString());
            }
        });

        // ====================================================
        // ERROR
        // ====================================================
        store.registerReducer(InvoiceAction.INVOICE_ERROR.toString(), payload -> {
            store.getAppState().set("InvoiceError",
                    payload != null ? payload.toString() : "Có lỗi xảy ra");
        });

        // ====================================================
        // CLEAR MESSAGE
        // ====================================================
        store.registerReducer(InvoiceAction.INVOICE_CLEAR_MESSAGE.toString(), payload -> {
            store.getAppState().set("InvoiceMessage", "");
            store.getAppState().set("InvoiceError", "");
        });
    }

    // ====================================================
    // Helper
    // ====================================================
    @SuppressWarnings("unchecked")
    private static void updateInvoiceList(Store store, Object payload) {

        if (payload instanceof List<?> raw) {
            List<InvoiceInfo> list = raw.stream()
                    .map(item -> InvoiceInfo.fromMap((Map<String, Object>) item))
                    .collect(Collectors.toList());

            store.getAppState().set("Invoices", list);
        }
    }
}
