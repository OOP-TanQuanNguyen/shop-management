package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.UserModel;
import edu.ptithcm.services.admin.EmployeeService;
import edu.ptithcm.views.admin.EmployeePanel;
import edu.ptithcm.views.components.AppMessageBox;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

/**
 * Controller FE điều phối giữa View (EmployeePanel) và Service
 * (EmployeeService). - Gửi yêu cầu CRUD đến BE qua DTTP. - Nhận dữ liệu từ
 * Store và cập nhật UI. - KHÔNG tạo giao diện, chỉ xử lý logic và điều phối.
 */
public class EmployeeController {

    private final EmployeePanel view;
    private final EmployeeService service;
    private final Store store = Store.getInstance();

    public EmployeeController(EmployeePanel view, EmployeeService service) {
        this.view = view;
        this.service = service;

        // Đăng ký event listener
        registerEvents();

        // Theo dõi thay đổi từ Store
        store.subcribe(this::onStateChanged);

        // Lấy danh sách ban đầu
        loadEmployees();
    }

    // ============================================================
    // Gắn sự kiện từ các nút trong View
    // ============================================================
    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());
        view.getBtnReload().addActionListener(e -> loadEmployees());
    }

    // ============================================================
    // Load dữ liệu
    // ============================================================
    private void loadEmployees() {
        try {
            service.getAllEmployees();
        } catch (IOException e) {
            AppMessageBox.showError("Không thể tải danh sách nhân viên: " + e.getMessage());
        }
    }

    // ============================================================
    // Các handler xử lý tương tác
    // ============================================================
    private void handleAdd() {
        // TODO: Mở dialog thêm nhân viên
        // Ví dụ: EmployeeAddDialog dialog = new EmployeeAddDialog(view);
        // if (dialog.showDialog()) {
        //     EmployeeData data = dialog.getData();
        //     createEmployee(data);
        // }

        // Tạm thời dùng code cũ để test
        JTextField txtUsername = new JTextField();
        JTextField txtPassword = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtRole = new JTextField();

        Object[] fields = {
            "Tên đăng nhập:", txtUsername,
            "Mật khẩu:", txtPassword,
            "Tên nhân viên:", txtName,
            "Số điện thoại:", txtPhone,
            "Chức vụ:", txtRole
        };

        int result = JOptionPane.showConfirmDialog(view, fields, "Thêm nhân viên", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            createEmployee(
                    txtUsername.getText().trim(),
                    txtPassword.getText().trim(),
                    txtName.getText().trim(),
                    txtPhone.getText().trim(),
                    txtRole.getText().trim()
            );
        }
    }

    private void handleEdit() {
        JTable table = view.getTable();
        int row = table.getSelectedRow();

        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn nhân viên để sửa!");
            return;
        }

        // TODO: Mở dialog sửa nhân viên với dữ liệu từ row
        // Ví dụ: EmployeeEditDialog dialog = new EmployeeEditDialog(view, employeeData);
        // if (dialog.showDialog()) {
        //     EmployeeData data = dialog.getData();
        //     updateEmployee(data);
        // }
        // Tạm thời dùng code cũ
        String id = String.valueOf(table.getValueAt(row, 0));
        String oldName = String.valueOf(table.getValueAt(row, 1));
        String oldRole = String.valueOf(table.getValueAt(row, 3));
        String oldPhone = String.valueOf(table.getValueAt(row, 4));
        String oldStatus = String.valueOf(table.getValueAt(row, 6));

        JTextField txtName = new JTextField(oldName);
        JTextField txtPhone = new JTextField(oldPhone);
        JTextField txtRole = new JTextField(oldRole);
        JCheckBox chkStatus = new JCheckBox("Đang làm việc", "Đang làm việc".equals(oldStatus));

        Object[] fields = {
            "Tên nhân viên:", txtName,
            "Số điện thoại:", txtPhone,
            "Chức vụ:", txtRole,
            "Trạng thái:", chkStatus
        };

        int result = JOptionPane.showConfirmDialog(view, fields, "Cập nhật nhân viên", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            updateEmployee(
                    id,
                    txtName.getText().trim(),
                    txtPhone.getText().trim(),
                    txtRole.getText().trim(),
                    chkStatus.isSelected()
            );
        }
    }

    private void handleDelete() {
        JTable table = view.getTable();
        int row = table.getSelectedRow();

        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn nhân viên để xóa!");
            return;
        }

        String id = String.valueOf(table.getValueAt(row, 0));
        String name = String.valueOf(table.getValueAt(row, 1));

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn xóa nhân viên: " + name + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            deleteEmployee(id);
        }
    }

    // ============================================================
    // Business Logic - Gọi Service
    // ============================================================
    private void createEmployee(String username, String password, String name, String phone, String role) {
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || role.isEmpty()) {
            AppMessageBox.showWarning("Vui lòng điền đầy đủ thông tin bắt buộc!");
            return;
        }

        try {
            service.createEmployee(username, password, name, phone, role);
        } catch (IOException e) {
            AppMessageBox.showError("Lỗi khi thêm nhân viên: " + e.getMessage());
        }
    }

    private void updateEmployee(String id, String name, String phone, String role, Boolean status) {
        try {
            service.updateEmployee(id, name, phone, role, status);
        } catch (IOException e) {
            AppMessageBox.showError("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    private void deleteEmployee(String id) {
        try {
            service.deleteEmployee(id);
        } catch (IOException e) {
            AppMessageBox.showError("Lỗi khi xóa nhân viên: " + e.getMessage());
        }
    }

    // ============================================================
    // Nhận dữ liệu mới từ Store và cập nhật UI
    // ============================================================
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            // Cập nhật danh sách nhân viên
            Object empListObj = state.get("Employees");
            if (empListObj instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<UserModel> list = (List<UserModel>) empListObj;
                view.updateTable(list);
            }

            // Hiển thị thông báo (nếu có)
            Object msg = state.get("EmployeeMessage");
            if (msg instanceof String) {
                String message = (String) msg;
                if (!message.isEmpty()) {
                    if (message.contains("thành công")) {
                        AppMessageBox.showSuccess(message);
                    } else {
                        AppMessageBox.showInfo(message);
                    }
                }
            }

            // Hiển thị lỗi (nếu có)
            Object err = state.get("EmployeeError");
            if (err instanceof String) {
                String error = (String) err;
                if (!error.isEmpty()) {
                    AppMessageBox.showError(error);
                }
            }

            // Reset messages sau khi hiển thị
            state.set("EmployeeMessage", "");
            state.set("EmployeeError", "");
        });
    }
}
