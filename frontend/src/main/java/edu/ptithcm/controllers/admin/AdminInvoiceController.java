package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.InvoiceInfo;
import edu.ptithcm.services.pos.InvoiceService;
import edu.ptithcm.views.admin.AdminInvoicePanel;
import edu.ptithcm.views.components.AppMessageBox;
import java.awt.Frame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class AdminInvoiceController {

    private final AdminInvoicePanel view;
    private final InvoiceService service;
    private final Store store = Store.getInstance();

    private boolean isShowingMessage = false;
    private List<InvoiceInfo> currentInvoices = new ArrayList<>();

    public AdminInvoiceController(
        AdminInvoicePanel view,
        InvoiceService service
    ) {
        this.view = view;
        this.service = service;

        registerEvents();
        store.subcribe(this::onStateChanged);

        reloadInvoices();
    }

    /* ============================================================
       REGISTER EVENTS
    ============================================================ */
    private void registerEvents() {
        view.getBtnReload().addActionListener(e -> reloadInvoices());
        view.getBtnDelete().addActionListener(e -> handleDelete());
    }

    /* ============================================================
       RELOAD
    ============================================================ */
    private void reloadInvoices() {
        try {
            service.getAllInvoices(); // gửi request
        } catch (IOException e) {
            AppMessageBox.showError(
                "Không thể tải danh sách hóa đơn: " + e.getMessage()
            );
        }
    }

    /* ============================================================
       DELETE
    ============================================================ */
    private void handleDelete() {
        String id = view.getSelectedInvoiceId();

        if (id == null) {
            AppMessageBox.showWarning("Hãy chọn hóa đơn để xóa!");
            return;
        }

        int confirm = AppMessageBox.showConfirm(
            "Bạn có chắc chắn muốn xóa hóa đơn " + id + " ?"
        );

        if (confirm != AppMessageBox.YES) {
            return;
        }

        try {
            service.deleteInvoice(id);
        } catch (Exception e) {
            AppMessageBox.showError("Không thể xóa hóa đơn: " + e.getMessage());
        }
    }

    /* ============================================================
       STATE MANAGER
    ============================================================ */
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateInvoiceList(state);
            showMessages(state);
        });
    }

    @SuppressWarnings("unchecked")
    private void updateInvoiceList(AppState state) {
        Object listObj = state.get("Invoices");

        view.updateTable((List<InvoiceInfo>) listObj);
    }

    /* ============================================================
       SHOW MESSAGE
    ============================================================ */
    private void showMessages(AppState state) {
        if (isShowingMessage) {
            return;
        }

        String successMsg = (String) state.get("InvoiceMessage");
        if (successMsg != null && !successMsg.isEmpty()) {
            isShowingMessage = true;
            state.set("InvoiceMessage", "");
            AppMessageBox.showSuccess(successMsg);
            isShowingMessage = false;
            return;
        }

        String errorMsg = (String) state.get("InvoiceError");
        if (errorMsg != null && !errorMsg.isEmpty()) {
            isShowingMessage = true;
            state.set("InvoiceError", "");
            AppMessageBox.showError(errorMsg);
            isShowingMessage = false;
        }
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
