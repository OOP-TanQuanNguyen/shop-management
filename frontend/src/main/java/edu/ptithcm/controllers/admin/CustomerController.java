package edu.ptithcm.controllers.admin;

import java.awt.Frame;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.services.admin.CustomerService;
import edu.ptithcm.views.admin.CustomerPanel;
import edu.ptithcm.views.admin.customer_dialogs.CustomerAddDialog;
import edu.ptithcm.views.admin.customer_dialogs.CustomerEditDialog;
import edu.ptithcm.views.admin.customer_dialogs.CustomerDeleteConfirmDialog;
import edu.ptithcm.views.components.AppMessageBox;

public class CustomerController {

    private static final Logger logger = Logger.getLogger(CustomerController.class.getName());

    private final CustomerPanel view;
    private final CustomerService service;
    private final Store store = Store.getInstance();

    private boolean isShowingMessage = false;
    private List<CustomerModel> currentCustomers; // ✅ Cache danh sách hiện tại

    public CustomerController(CustomerPanel view, CustomerService service) {
        this.view = view;
        this.service = service;

        registerEvents();
        store.subcribe(this::onStateChanged);
        loadCustomers();
    }

    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());
        view.getBtnReload().addActionListener(e -> loadCustomers());
    }

    private void loadCustomers() {
        try {
            service.getAllCustomers();
        } catch (IOException e) {
            AppMessageBox.showError("Không thể tải danh sách: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // CRUD
    // ─────────────────────────────────────────────
    private void handleAdd() {
        CustomerAddDialog dl = new CustomerAddDialog(getParentFrame());
        dl.showDialog();

        if (dl.isConfirmed()) {
            try {
                service.createCustomer(
                        dl.getCustomerName(),
                        dl.getPhone(),
                        dl.getPoint()
                );
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi: " + e.getMessage());
            }
        }
    }

    private void handleEdit() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn khách hàng!");
            return;
        }

        // ✅ Lấy customer từ list cache thay vì từ table
        if (currentCustomers == null || row >= currentCustomers.size()) {
            AppMessageBox.showError("Dữ liệu không hợp lệ!");
            return;
        }

        CustomerModel customer = currentCustomers.get(row);
        String customerId = customer.getId();
        String name = customer.getName();
        String phone = customer.getPhone();
        int point = customer.getPoint() != null ? customer.getPoint() : 0;

        CustomerEditDialog dl = new CustomerEditDialog(getParentFrame(), customerId, name, phone, point);
        dl.showDialog();

        if (dl.isConfirmed()) {
            try {
                // ✅ KHÔNG gửi point - chỉ update name và phone
                service.updateCustomer(
                        customerId,
                        dl.getCustomerName(),
                        dl.getPhone(),
                        null // ✅ Không update điểm tích lũy
                );
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi cập nhật: " + e.getMessage());
            }
        }
    }

    private void handleDelete() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn khách hàng!");
            return;
        }

        // ✅ Lấy customer từ list cache thay vì từ table
        if (currentCustomers == null || row >= currentCustomers.size()) {
            AppMessageBox.showError("Dữ liệu không hợp lệ!");
            return;
        }

        CustomerModel customer = currentCustomers.get(row);
        String customerId = customer.getId();
        String name = customer.getName();

        CustomerDeleteConfirmDialog dl = new CustomerDeleteConfirmDialog(getParentFrame(), name);
        dl.showDialog();

        if (dl.isConfirmed()) {
            try {
                service.deleteCustomer(customerId);
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi xóa: " + e.getMessage());
            }
        }
    }

    // ─────────────────────────────────────────────
    // STATE CHANGE
    // ─────────────────────────────────────────────
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateCustomerList(state);
            showMessages(state);
        });
    }

    @SuppressWarnings("unchecked")
    private void updateCustomerList(AppState state) {
        Object listObj = state.get("Customers");
        if (listObj instanceof List<?> list) {
            currentCustomers = (List<CustomerModel>) list; // ✅ Cache lại
            view.updateTable(currentCustomers);
        }
    }

    private void showMessages(AppState state) {
        if (isShowingMessage) {
            return;
        }

        String msg = (String) state.get("CustomerMessage");
        if (msg != null && !msg.isEmpty()) {
            isShowingMessage = true;

            state.set("CustomerMessage", "");
            AppMessageBox.showSuccess(msg);

            isShowingMessage = false;
            return;
        }

        String err = (String) state.get("CustomerError");
        if (err != null && !err.isEmpty()) {
            isShowingMessage = true;

            state.set("CustomerError", "");
            AppMessageBox.showError(err);

            isShowingMessage = false;
        }
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
