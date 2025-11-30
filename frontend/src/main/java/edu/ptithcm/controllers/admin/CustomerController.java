package edu.ptithcm.controllers.admin;

import java.awt.Frame;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.services.admin.CustomerService;
import edu.ptithcm.services.admin.LoyaltyService;
import edu.ptithcm.views.admin.CustomerPanel;
import edu.ptithcm.views.admin.customer_dialogs.CustomerAddDialog;
import edu.ptithcm.views.admin.customer_dialogs.CustomerEditDialog;
import edu.ptithcm.views.admin.customer_dialogs.CustomerDeleteConfirmDialog;
import edu.ptithcm.views.components.AppMessageBox;

public class CustomerController {

    private final CustomerPanel view;
    private final CustomerService service;
    private final LoyaltyService loyaltyService;

    private final Store store = Store.getInstance();

    private boolean isShowingMessage = false;
    private List<CustomerModel> currentCustomers;

    public CustomerController(
            CustomerPanel view,
            CustomerService service,
            LoyaltyService loyaltyService
    ) {
        this.view = view;
        this.service = service;
        this.loyaltyService = loyaltyService;

        registerEvents();
        store.subcribe(this::onStateChanged);

        loadCustomers();
    }

    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());
        view.getBtnReload().addActionListener(e -> loadCustomers());
        view.getBtnFilter().addActionListener(e -> applyFilter());
    }

    private void loadCustomers() {
        view.getTxtSearch().setText("");
        try {
            service.getAllCustomers();
        } catch (IOException e) {
            AppMessageBox.showError("Không thể tải danh sách: " + e.getMessage());
        }
    }

    private void applyFilter() {

        if (currentCustomers == null) {
            return;
        }

        String keyword = view.getTxtSearch().getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            view.updateTable(currentCustomers);
            return;
        }

        List<CustomerModel> filtered = currentCustomers.stream()
                .filter(c -> {
                    String name = c.getName() != null ? c.getName().toLowerCase() : "";
                    String phone = c.getPhone() != null ? c.getPhone() : "";
                    return name.contains(keyword) || phone.startsWith(keyword);
                })
                .collect(Collectors.toList());

        view.updateTable(filtered);
    }

    // ============================================================
    // ADD CUSTOMER — FIXED LOYALTY LOGIC
    // ============================================================
    private void handleAdd() {

        CustomerAddDialog dl = new CustomerAddDialog(getParentFrame());
        dl.showDialog();

        if (!dl.isConfirmed()) {
            return;
        }

        try {
            // 1. Tạo customer
            service.createCustomer(dl.getCustomerName(), dl.getPhone(), dl.getPoint());

            // 2. Chờ customer mới xuất hiện trong Store
            CustomerModel newCustomer = waitForCustomerByPhone(dl.getPhone(), 3000);

            if (newCustomer != null && newCustomer.getId() != null) {

                // 3. Tạo Loyalty bằng customerId (KHÔNG dùng phone)
                loyaltyService.createLoyalty(newCustomer.getId());

            } else {
                AppMessageBox.showWarning(
                        "Không thể tạo Loyalty vì không lấy được ID khách hàng mới."
                );
            }

        } catch (IOException e) {
            AppMessageBox.showError("Lỗi: " + e.getMessage());
        }
    }

    private void handleEdit() {

        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn khách hàng!");
            return;
        }

        CustomerModel c = getSelectedCustomer(row);
        if (c == null) {
            return;
        }

        CustomerEditDialog dl = new CustomerEditDialog(
                getParentFrame(), c.getId(), c.getName(), c.getPhone(), c.getPoint()
        );

        dl.showDialog();

        if (dl.isConfirmed()) {
            try {
                service.updateCustomer(
                        c.getId(),
                        dl.getCustomerName(),
                        dl.getPhone(),
                        null
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

        CustomerModel c = getSelectedCustomer(row);
        if (c == null) {
            return;
        }

        CustomerDeleteConfirmDialog dl = new CustomerDeleteConfirmDialog(
                getParentFrame(), c.getName()
        );

        dl.showDialog();

        if (dl.isConfirmed()) {
            try {
                service.deleteCustomer(c.getId());
                loyaltyService.deleteLoyalty(c.getId());  // xóa loyalty luôn
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi xóa: " + e.getMessage());
            }
        }
    }

    private CustomerModel getSelectedCustomer(int row) {
        if (currentCustomers == null || row >= currentCustomers.size()) {
            AppMessageBox.showError("Dữ liệu không hợp lệ!");
            return null;
        }
        return currentCustomers.get(row);
    }

    // ============================================================
    // STATE MANAGEMENT
    // ============================================================
    @SuppressWarnings("unchecked")
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
            currentCustomers = (List<CustomerModel>) list;

            if (view.getTxtSearch().getText().trim().isEmpty()) {
                view.updateTable(currentCustomers);
            } else {
                applyFilter();
            }
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

    // ============================================================
    // SUPPORT: CHỜ CUSTOMER MỚI XUẤT HIỆN TRONG STORE
    // ============================================================
    @SuppressWarnings("unchecked")
    private CustomerModel waitForCustomerByPhone(String phone, long timeoutMs) {

        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeoutMs) {

            Object listObj = store.getAppState().get("Customers");

            if (listObj instanceof List<?> list) {
                for (Object o : list) {
                    CustomerModel c = (CustomerModel) o;
                    if (phone.equals(c.getPhone())) {
                        return c;
                    }
                }
            }

            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        return null;
    }
}
