package edu.ptithcm.controllers.admin;

import java.awt.Frame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.BranchInfo;
import edu.ptithcm.models.UserModel;
import edu.ptithcm.services.admin.BranchService;
import edu.ptithcm.services.admin.EmployeeService;
import edu.ptithcm.views.admin.EmployeePanel;
import edu.ptithcm.views.admin.employee_dialogs.EmployeeAddDialog;
import edu.ptithcm.views.admin.employee_dialogs.EmployeeDeleteConfirmDialog;
import edu.ptithcm.views.admin.employee_dialogs.EmployeeEditDialog;
import edu.ptithcm.views.admin.employee_dialogs.EmployeeFormDialog;
import edu.ptithcm.views.components.AppMessageBox;

public class EmployeeController {

    private final EmployeePanel view;
    private final EmployeeService service;
    private final BranchService branchService;
    private final Store store = Store.getInstance();

    private boolean isShowingMessage = false;

    private List<UserModel> currentEmployees = new ArrayList<>();
    private List<EmployeeFormDialog.BranchItem> branches = new ArrayList<>();

    public EmployeeController(EmployeePanel view,
            EmployeeService service,
            BranchService branchService) {

        this.view = view;
        this.service = service;
        this.branchService = branchService;

        registerEvents();

        store.subcribe(this::onStateChanged);

        loadBranches();
        loadEmployees();
    }

    // =====================================================================
    // EVENT REGISTRATION
    // =====================================================================
    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());
        view.getBtnReload().addActionListener(e -> loadEmployees());
    }

    private void loadBranches() {
        try {
            branchService.getAllBranches();
        } catch (Exception e) {
            AppMessageBox.showError("Không thể tải danh sách chi nhánh: " + e.getMessage());
        }
    }

    private void loadEmployees() {
        try {
            service.getAllEmployees();
        } catch (IOException e) {
            AppMessageBox.showError("Không thể tải danh sách nhân viên: " + e.getMessage());
        }
    }

    // =====================================================================
    // ADD EMPLOYEE
    // =====================================================================
    private void handleAdd() {

        if (branches == null || branches.isEmpty()) {
            AppMessageBox.showWarning("Danh sách chi nhánh đang được tải. Vui lòng thử lại sau.");
            return;
        }

        EmployeeAddDialog dialog = new EmployeeAddDialog(getParentFrame(), branches);
        dialog.showDialog();

        if (dialog.isConfirmed()) {
            try {
                service.createEmployee(
                        dialog.getUsername(),
                        dialog.getPassword(),
                        dialog.getEmployeeName(),
                        dialog.getPhone(),
                        dialog.getRole(),
                        dialog.getBranchId()
                );
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi thêm nhân viên: " + e.getMessage());
            }
        }
    }

    // =====================================================================
    // EDIT EMPLOYEE
    // =====================================================================
    private void handleEdit() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn nhân viên để chỉnh sửa!");
            return;
        }

        if (currentEmployees == null || row >= currentEmployees.size()) {
            AppMessageBox.showError("Dữ liệu không hợp lệ!");
            return;
        }

        if (branches == null || branches.isEmpty()) {
            AppMessageBox.showWarning("Danh sách chi nhánh đang được tải. Vui lòng thử lại sau.");
            return;
        }

        UserModel employee = currentEmployees.get(row);

        EmployeeEditDialog dialog = new EmployeeEditDialog(
                getParentFrame(),
                employee.getId(),
                employee.getName(),
                employee.getPhone(),
                employee.getRole(),
                employee.getBranchId(),
                employee.getBranch(),
                Boolean.TRUE.equals(employee.getStatus()),
                branches
        );

        dialog.showDialog();

        if (dialog.isConfirmed()) {
            try {
                service.updateEmployee(
                        dialog.getEmployeeId(),
                        dialog.getEmployeeName(),
                        dialog.getPhone(),
                        dialog.getRole(),
                        dialog.getBranchId(),
                        dialog.getStatus()
                );
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi cập nhật nhân viên: " + e.getMessage());
            }
        }
    }

    // =====================================================================
    // DELETE EMPLOYEE
    // =====================================================================
    private void handleDelete() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn nhân viên để xóa!");
            return;
        }

        if (currentEmployees == null || row >= currentEmployees.size()) {
            AppMessageBox.showError("Dữ liệu không hợp lệ!");
            return;
        }

        UserModel employee = currentEmployees.get(row);

        EmployeeDeleteConfirmDialog dialog
                = new EmployeeDeleteConfirmDialog(getParentFrame(), employee.getName());

        dialog.showDialog();

        if (dialog.isConfirmed()) {
            try {
                service.deleteEmployee(employee.getId());
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi xóa nhân viên: " + e.getMessage());
            }
        }
    }

    // =====================================================================
    // STATE LISTENER
    // =====================================================================
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateEmployeeList(state);
            updateBranchList(state);
            showMessages(state);
        });
    }

    @SuppressWarnings("unchecked")
    private void updateEmployeeList(AppState state) {
        Object listObj = state.get("Employees");

        if (listObj instanceof List<?> list) {
            currentEmployees = (List<UserModel>) list;
            view.updateTable(currentEmployees);
        }
    }

    // =====================================================================
    // UPDATE BRANCH LIST (CHUẨN BranchInfo)
    // =====================================================================
    private void updateBranchList(AppState state) {

        Object branchObj = state.get("Branches");

        if (branchObj instanceof List<?> list) {

            branches = new ArrayList<>();

            for (Object item : list) {
                if (item instanceof BranchInfo b) {

                    Integer branchId = null;

                    // Convert String → Integer (an toàn)
                    try {
                        if (b.getId() != null && !b.getId().isBlank()) {
                            branchId = Integer.parseInt(b.getId());
                        }
                    } catch (Exception ignored) {
                    }

                    branches.add(new EmployeeFormDialog.BranchItem(
                            branchId, // luôn là INTEGER cho ComboBox
                            b.getName()
                    ));
                }
            }

            System.out.println("[DEBUG] Branch list loaded: " + branches.size());
        }
    }

    // =====================================================================
    // MESSAGES
    // =====================================================================
    private void showMessages(AppState state) {

        if (isShowingMessage) {
            return;
        }

        String successMsg = (String) state.get("EmployeeMessage");
        if (successMsg != null && !successMsg.isEmpty()) {

            isShowingMessage = true;
            state.set("EmployeeMessage", "");
            AppMessageBox.showSuccess(successMsg);
            isShowingMessage = false;
            return;
        }

        String errorMsg = (String) state.get("EmployeeError");
        if (errorMsg != null && !errorMsg.isEmpty()) {

            isShowingMessage = true;
            state.set("EmployeeError", "");
            AppMessageBox.showError(errorMsg);
            isShowingMessage = false;
        }
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
