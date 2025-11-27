package edu.ptithcm.controllers.pos;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.InvoiceInfo;
import edu.ptithcm.services.pos.InvoiceService;
import edu.ptithcm.views.pos.panels.MyInvoicePanel;

import javax.swing.*;
import java.util.List;
import java.util.logging.Logger;

public class InvoiceController {

    private static final Logger logger = Logger.getLogger(InvoiceController.class.getName());

    private final Store store = Store.getInstance();
    private final InvoiceService service;

    private final MyInvoicePanel view;
    private List<InvoiceInfo> invoiceList;
    private boolean isShowingMessage = false;

    public InvoiceController(MyInvoicePanel view, InvoiceService service) {
        this.view = view;
        this.service = service;

        registerEvents();
        store.subcribe(this::onStateChanged);

        // Load hóa đơn lần đầu
        try {
            service.getInvoicesForEmployee();
        } catch (Exception ignored) {
        }
    }

    /* ==========================================================
       REGISTER EVENTS
    ========================================================== */
    private void registerEvents() {

        view.getBtnReload().addActionListener(e -> {
            try {
                service.getInvoicesForEmployee();
            } catch (Exception ex) {
                view.showMessage("Không thể tải lại hóa đơn.");
            }
        });

        view.getBtnConfirm().addActionListener(e -> handleConfirm());
        view.getBtnCancel().addActionListener(e -> handleCancel());
    }

    /* ==========================================================
       CONFIRM
    ========================================================== */
    private void handleConfirm() {

        String id = view.getSelectedInvoiceId();
        if (id == null) {
            view.showMessage("Hãy chọn hóa đơn cần xác nhận");
            return;
        }

        try {
            service.confirmInvoice(id);
        } catch (Exception e) {
            view.showMessage("Không thể xác nhận hóa đơn");
        }
    }

    /* ==========================================================
       CANCEL
    ========================================================== */
    private void handleCancel() {

        String id = view.getSelectedInvoiceId();
        if (id == null) {
            view.showMessage("Hãy chọn hóa đơn để hủy");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn chắc chắn muốn hủy hóa đơn này?",
                "Hủy hóa đơn",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            service.cancelInvoice(id);
        } catch (Exception e) {
            view.showMessage("Không thể hủy hóa đơn");
        }
    }

    /* ==========================================================
       STATE LISTENER
    ========================================================== */
    @SuppressWarnings("unchecked")
    private void onStateChanged(AppState state) {

        SwingUtilities.invokeLater(() -> {
            updateInvoiceList(state);
            showMessagesFromState(state);
        });
    }

    /* ==========================================================
       UPDATE TABLE LIST
    ========================================================== */
    private void updateInvoiceList(AppState state) {

        Object obj = state.get("Invoices");  // KEY CHUẨN từ InvoiceReducer

        if (obj instanceof List<?> list) {
            invoiceList = (List<InvoiceInfo>) list;
            view.updateTable(invoiceList);
        }
    }

    /* ==========================================================
       SHOW MESSAGES
    ========================================================== */
    private void showMessagesFromState(AppState state) {

        if (isShowingMessage) {
            return;
        }

        String msg = (String) state.get("InvoiceMessage");
        if (msg != null && !msg.isEmpty()) {

            isShowingMessage = true;
            state.set("InvoiceMessage", "");

            JOptionPane.showMessageDialog(view, msg);
            isShowingMessage = false;

            return;
        }

        String err = (String) state.get("InvoiceError");
        if (err != null && !err.isEmpty()) {

            isShowingMessage = true;
            state.set("InvoiceError", "");

            JOptionPane.showMessageDialog(view, err, "Lỗi", JOptionPane.ERROR_MESSAGE);
            isShowingMessage = false;
        }
    }
}
