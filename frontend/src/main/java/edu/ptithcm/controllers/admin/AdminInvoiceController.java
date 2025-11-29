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
            service.getAllInvoices();
        } catch (IOException e) {
            AppMessageBox.showError(
                    "Không thể tải danh sách hóa đơn: " + e.getMessage()
            );
        }
    }

    /* ============================================================
       ✅ FIX: DELETE + AUTO RELOAD
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

            // ✅ TỰ ĐỘNG RELOAD SAU KHI XÓA THÀNH CÔNG
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(200); // Đợi server xử lý xong
                    service.getAllInvoices();
                    AppMessageBox.showSuccess("Xóa hóa đơn thành công!");
                } catch (Exception ex) {
                    AppMessageBox.showError("Không thể tải lại danh sách: " + ex.getMessage());
                }
            });

        } catch (Exception e) {
            AppMessageBox.showError("Không thể xóa hóa đơn: " + e.getMessage());
        }
    }

    /* ============================================================
       ✅ FIX: STATE MANAGER - CHẶN MESSAGE TỪ POS
    ============================================================ */
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateInvoiceList(state);

            // ✅ CHẶN MESSAGE TỪ POS - KHÔNG HIỂN THỊ
            clearMessages(state);
        });
    }

    @SuppressWarnings("unchecked")
    private void updateInvoiceList(AppState state) {
        Object listObj = state.get("Invoices");
        view.updateTable((List<InvoiceInfo>) listObj);
    }

    /* ============================================================
       ✅ FIX: XÓA MESSAGE THAY VÌ HIỂN THỊ (CHẶN MESSAGE TỪ POS)
    ============================================================ */
    private void clearMessages(AppState state) {
        // Xóa message mà không hiển thị (bao gồm message từ POS)
        state.set("InvoiceMessage", "");
        state.set("InvoiceError", "");
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
