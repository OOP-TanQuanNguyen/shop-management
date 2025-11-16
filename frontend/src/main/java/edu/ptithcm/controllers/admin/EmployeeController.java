package edu.ptithcm.controllers.admin;

import java.awt.Frame;
import java.io.IOException;
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

public class EmployeeController {

    private static final Logger logger = Logger.getLogger(EmployeeController.class.getName());

    private final EmployeePanel view;
    private final EmployeeService service;
    private final Store store = Store.getInstance();

    private boolean isShowingMessage = false;

    public EmployeeController(EmployeePanel view, EmployeeService service) {
        this.view = view;
        this.service = service;

        registerEvents();
        store.subcribe(this::onStateChanged);
        loadEmployees();
    }

    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());
        view.getBtnReload().addActionListener(e -> loadEmployees());
    }

    private void loadEmployees() {
        try {
            service.getAllEmployees();
        } catch (IOException e) {
            AppMessageBox.showError("Không thể tải danh sách: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    // CRUD
    // ─────────────────────────────────────────────
    private void handleAdd() {
        EmployeeAddDialog dl = new EmployeeAddDialog(getParentFrame());
        dl.showDialog();

        if (dl.isConfirmed()) {
            try {
                service.createEmployee(
                        dl.getUsername(),
                        dl.getPassword(),
                        dl.getEmployeeName(),
                        dl.getPhone(),
                        dl.getRole()
                );
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi: " + e.getMessage());
            }
        }
    }

    private void handleEdit() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn nhân viên!");
            return;
        }

        String id = view.getTable().getValueAt(row, 0).toString();
        String name = view.getTable().getValueAt(row, 1).toString();
        String role = view.getTable().getValueAt(row, 3).toString();
        String phone = view.getTable().getValueAt(row, 4).toString();
        boolean status = "Đang làm việc".equals(view.getTable().getValueAt(row, 6));

        EmployeeEditDialog dl = new EmployeeEditDialog(getParentFrame(), id, name, phone, role, status);
        dl.showDialog();

        if (dl.isConfirmed()) {
            try {
                service.updateEmployee(id, dl.getEmployeeName(), dl.getPhone(), dl.getRole(), dl.getStatus());
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi cập nhật: " + e.getMessage());
            }
        }
    }

    private void handleDelete() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn nhân viên!");
            return;
        }

        String id = view.getTable().getValueAt(row, 0).toString();
        String name = view.getTable().getValueAt(row, 1).toString();

        EmployeeDeleteConfirmDialog dl = new EmployeeDeleteConfirmDialog(getParentFrame(), name);
        dl.showDialog();

        if (dl.isConfirmed()) {
            try {
                service.deleteEmployee(id);
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
            updateEmployeeList(state);
            showMessages(state);
        });
    }

    private void updateEmployeeList(AppState state) {
        Object listObj = state.get("Employees");
        if (listObj instanceof List<?> list) {
            view.updateTable((List<UserModel>) list);
        }
    }

    private void showMessages(AppState state) {
        if (isShowingMessage) {
            return;
        }

        String msg = (String) state.get("EmployeeMessage");
        if (msg != null && !msg.isEmpty()) {
            isShowingMessage = true;

            state.set("EmployeeMessage", "");
            AppMessageBox.showSuccess(msg);

            isShowingMessage = false;
            return;
        }

        String err = (String) state.get("EmployeeError");
        if (err != null && !err.isEmpty()) {
            isShowingMessage = true;

            state.set("EmployeeError", "");
            AppMessageBox.showError(err);

            isShowingMessage = false;
        }
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
