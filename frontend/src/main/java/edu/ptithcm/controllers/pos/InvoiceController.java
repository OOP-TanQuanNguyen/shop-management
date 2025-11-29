package edu.ptithcm.controllers.pos;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.InvoiceInfo;
import edu.ptithcm.services.pos.InvoiceService;
import edu.ptithcm.views.pos.panels.MyInvoicePanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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

        // ==== FIX QUAN TRỌNG: Kiểm tra trạng thái khi chọn hàng ====
        view.getTable().getSelectionModel().addListSelectionListener(e -> updateButtons());
    }

    /* ==========================================================
       UPDATE BUTTON STATUS
    ========================================================== */
    private void updateButtons() {
        int row = view.getTable().getSelectedRow();
        if (row == -1 || invoiceList == null) {
            view.getBtnConfirm().setEnabled(false);
            view.getBtnCancel().setEnabled(false);
            return;
        }

        String id = (String) view.getTable().getValueAt(row, 0);
        InvoiceInfo inv = findInvoiceById(id);

        if (inv == null) {
            view.getBtnConfirm().setEnabled(false);
            view.getBtnCancel().setEnabled(false);
            return;
        }

        boolean pending = "PENDING".equals(inv.getStatus());

        view.getBtnConfirm().setEnabled(pending);
        view.getBtnCancel().setEnabled(pending);
    }

    private InvoiceInfo findInvoiceById(String id) {
        if (invoiceList == null) {
            return null;
        }
        for (InvoiceInfo i : invoiceList) {
            if (i.getInvoiceId().equals(id)) {
                return i;
            }
        }
        return null;
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

        InvoiceInfo inv = findInvoiceById(id);
        if (inv == null) {
            return;
        }

        if (!"PENDING".equals(inv.getStatus())) {
            JOptionPane.showMessageDialog(view,
                    "Hóa đơn đã hoàn thành hoặc đã hủy.\nKhông thể xác nhận thêm.",
                    "Không thể xác nhận",
                    JOptionPane.WARNING_MESSAGE);
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

        InvoiceInfo inv = findInvoiceById(id);
        if (inv == null) {
            return;
        }

        if (!"PENDING".equals(inv.getStatus())) {
            JOptionPane.showMessageDialog(view,
                    "Hóa đơn đã hoàn thành hoặc đã hủy.\nKhông thể hủy.",
                    "Không thể hủy",
                    JOptionPane.WARNING_MESSAGE);
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
            updateButtons();   // FIX: Sau khi reload, cần cập nhật nút
            showMessagesFromState(state);
        });
    }

    /* ==========================================================
       UPDATE TABLE LIST
    ========================================================== */
    private void updateInvoiceList(AppState state) {

        Object obj = state.get("Invoices");

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
