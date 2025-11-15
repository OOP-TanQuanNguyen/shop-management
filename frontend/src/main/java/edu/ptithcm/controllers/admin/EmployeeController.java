package edu.ptithcm.controllers.admin;

import java.awt.Frame;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.UserModel;
import edu.ptithcm.services.admin.EmployeeService;
import edu.ptithcm.views.admin.EmployeePanel;
import edu.ptithcm.views.admin.employee_dialogs.EmployeeAddDialog;
import edu.ptithcm.views.admin.employee_dialogs.EmployeeDeleteConfirmDialog;
import edu.ptithcm.views.admin.employee_dialogs.EmployeeEditDialog;
import edu.ptithcm.views.components.AppMessageBox;

/**
 * Controller điều phối giữa EmployeePanel và EmployeeService. KHÔNG chứa code
 * Swing - tất cả UI logic ở View layer.
 */
public class EmployeeController {

    private static final Logger logger = Logger.getLogger(EmployeeController.class.getName());

    private final EmployeePanel view;
    private final EmployeeService service;
    private final Store store = Store.getInstance();

    public EmployeeController(EmployeePanel view, EmployeeService service) {
        this.view = view;
        this.service = service;

        registerEvents();
        store.subcribe(this::onStateChanged);
        loadEmployees();
    }

    // ============================================================
    // Event Registration
    // ============================================================
    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());
        view.getBtnReload().addActionListener(e -> loadEmployees());
    }

    // ============================================================
    // Data Loading
    // ============================================================
    private void loadEmployees() {
        try {
            service.getAllEmployees();
        } catch (IOException e) {
            logger.severe("Failed to load employees: " + e.getMessage());
            AppMessageBox.showError("Không thể tải danh sách nhân viên: " + e.getMessage());
        }
    }

    // ============================================================
    // Event Handlers
    // ============================================================
    private void handleAdd() {
        // Mở dialog thêm nhân viên
        EmployeeAddDialog dialog = new EmployeeAddDialog(getParentFrame());
        dialog.showDialog();

        // Nếu user confirm, gọi service
        if (dialog.isConfirmed()) {
            createEmployee(
                    dialog.getUsername(),
                    dialog.getPassword(),
                    dialog.getEmployeeName(),
                    dialog.getPhone(),
                    dialog.getRole()
            );
        }
    }

    private void handleEdit() {
        // Lấy dòng được chọn
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn nhân viên để sửa!");
            return;
        }

        // Lấy dữ liệu từ table
        String id = String.valueOf(view.getTable().getValueAt(row, 0));
        String name = String.valueOf(view.getTable().getValueAt(row, 1));
        String role = String.valueOf(view.getTable().getValueAt(row, 3));
        String phone = String.valueOf(view.getTable().getValueAt(row, 4));
        String statusText = String.valueOf(view.getTable().getValueAt(row, 6));
        boolean status = "Đang làm việc".equals(statusText);

        // Mở dialog sửa
        EmployeeEditDialog dialog = new EmployeeEditDialog(getParentFrame(), id, name, phone, role, status);
        dialog.showDialog();

        // Nếu user confirm, gọi service
        if (dialog.isConfirmed()) {
            updateEmployee(
                    dialog.getEmployeeId(),
                    dialog.getEmployeeName(),
                    dialog.getPhone(),
                    dialog.getRole(),
                    dialog.getStatus()
            );
        }
    }

    private void handleDelete() {
        // Lấy dòng được chọn
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn nhân viên để xóa!");
            return;
        }

        // Lấy thông tin nhân viên
        String id = String.valueOf(view.getTable().getValueAt(row, 0));
        String name = String.valueOf(view.getTable().getValueAt(row, 1));

        // Mở dialog xác nhận
        EmployeeDeleteConfirmDialog dialog = new EmployeeDeleteConfirmDialog(getParentFrame(), name);
        dialog.showDialog();

        // Nếu user confirm, gọi service
        if (dialog.isConfirmed()) {
            deleteEmployee(id);
        }
    }

    // ============================================================
    // Business Logic
    // ============================================================
    private void createEmployee(String username, String password, String name, String phone, String role) {
        try {
            service.createEmployee(username, password, name, phone, role);
            logger.info("Create employee request sent for: " + username);
        } catch (IOException e) {
            logger.severe("Failed to create employee: " + e.getMessage());
            AppMessageBox.showError("Lỗi khi thêm nhân viên: " + e.getMessage());
        }
    }

    private void updateEmployee(String id, String name, String phone, String role, Boolean status) {
        try {
            service.updateEmployee(id, name, phone, role, status);
            logger.info("Update employee request sent for ID: " + id);
        } catch (IOException e) {
            logger.severe("Failed to update employee: " + e.getMessage());
            AppMessageBox.showError("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    private void deleteEmployee(String id) {
        try {
            service.deleteEmployee(id);
            logger.info("Delete employee request sent for ID: " + id);
        } catch (IOException e) {
            logger.severe("Failed to delete employee: " + e.getMessage());
            AppMessageBox.showError("Lỗi khi xóa nhân viên: " + e.getMessage());
        }
    }

    // ============================================================
    // State Change Handler
    // ============================================================
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateEmployeeList(state);
            showMessages(state);
            clearMessages(state);
        });
    }

    private void updateEmployeeList(AppState state) {
        Object empListObj = state.get("Employees");
        if (empListObj instanceof List<?> list) {
            @SuppressWarnings("unchecked")
            List<UserModel> employees = (List<UserModel>) list;
            view.updateTable(employees);

            LocalTime now = LocalTime.now();
            String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            System.out.println("⏱ Update row thành công = " + time);
            logger.info("Employee list updated: " + employees.size() + " items");
        }
    }

    private void showMessages(AppState state) {
        // Show success/info message
        Object msg = state.get("EmployeeMessage");
        if (msg instanceof String message && !message.isEmpty()) {
            if (message.contains("thành công")) {
                AppMessageBox.showSuccess(message);
            } else {
                AppMessageBox.showInfo(message);
            }
        }

        // Show error message
        Object err = state.get("EmployeeError");
        if (err instanceof String error && !error.isEmpty()) {
            AppMessageBox.showError(error);
        }
    }

    private void clearMessages(AppState state) {
        state.set("EmployeeMessage", "");
        state.set("EmployeeError", "");
    }

    // ============================================================
    // Helper
    // ============================================================
    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
