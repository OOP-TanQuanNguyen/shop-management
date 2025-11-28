package edu.ptithcm.controllers.admin;

import java.awt.Frame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<String> lastBranchNames = new ArrayList<>();

    public EmployeeController(EmployeePanel view,
            EmployeeService service,
            BranchService branchService) {

        this.view = view;
        this.service = service;
        this.branchService = branchService;

        registerEvents();

        store.subcribe(this::onStateChanged);

        loadBranches();
        reloadEmployees(); // initial load
    }

    // =======================================================
    // RESET FILTERS – CHỈ DÙNG KHI BẤM TẢI LẠI
    // =======================================================
    private void resetFilters() {
        view.getCbRole().setSelectedIndex(0);
        view.getCbStatus().setSelectedIndex(0);
        view.getCbBranch().setSelectedIndex(0);
    }

    // =======================================================
    // REGISTER EVENTS
    // =======================================================
    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());

        // Reset filter chỉ khi nhấn "Tải lại"
        view.getBtnReload().addActionListener(e -> reloadEmployees());

        // Lọc: KHÔNG reset filter
        view.getBtnFilter().addActionListener(e -> handleFilter());
    }

    // =======================================================
    // RELOAD (RESET FILTER + GET ALL)
    // =======================================================
    private void reloadEmployees() {
        resetFilters();
        try {
            service.getAllEmployees();
        } catch (IOException e) {
            AppMessageBox.showError("Không thể tải danh sách nhân viên: " + e.getMessage());
        }
    }

    // =======================================================
    // LOAD BRANCHES
    // =======================================================
    private void loadBranches() {
        try {
            branchService.getAllBranches();
        } catch (Exception e) {
            AppMessageBox.showError("Không thể tải danh sách chi nhánh: " + e.getMessage());
        }
    }

    // =======================================================
    // FILTER EMPLOYEE
    // =======================================================
    private void handleFilter() {

        Map<String, Object> filters = new HashMap<>();

        // Role
        switch (view.getCbRole().getSelectedIndex()) {
            case 1 ->
                filters.put("role", "ADMIN");
            case 2 ->
                filters.put("role", "STAFF");
        }

        // Status
        switch (view.getCbStatus().getSelectedIndex()) {
            case 1 ->
                filters.put("status", true);
            case 2 ->
                filters.put("status", false);
        }

        // Branch
        int idx = view.getCbBranch().getSelectedIndex();
        if (idx > 0 && idx - 1 < branches.size()) {
            Integer branchId = branches.get(idx - 1).getId();
            if (branchId != null) {
                filters.put("branchId", branchId);
            }
        }

        try {
            service.filterEmployees(filters);
        } catch (Exception e) {
            AppMessageBox.showError("Không thể lọc nhân viên: " + e.getMessage());
        }
    }

    // =======================================================
    // CRUD HANDLERS
    // =======================================================
    private void handleAdd() {

        if (branches.isEmpty()) {
            AppMessageBox.showWarning("Danh sách chi nhánh chưa tải xong.");
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

    private void handleEdit() {

        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn nhân viên để chỉnh sửa!");
            return;
        }

        if (row >= currentEmployees.size()) {
            AppMessageBox.showError("Dữ liệu không hợp lệ!");
            return;
        }

        UserModel emp = currentEmployees.get(row);

        EmployeeEditDialog dialog = new EmployeeEditDialog(
                getParentFrame(),
                emp.getId(),
                emp.getName(),
                emp.getPhone(),
                emp.getRole(),
                emp.getBranchId(),
                emp.getBranch(),
                Boolean.TRUE.equals(emp.getStatus()),
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

    private void handleDelete() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn nhân viên để xóa!");
            return;
        }

        if (row >= currentEmployees.size()) {
            AppMessageBox.showError("Dữ liệu không hợp lệ!");
            return;
        }

        UserModel emp = currentEmployees.get(row);

        EmployeeDeleteConfirmDialog dialog
                = new EmployeeDeleteConfirmDialog(getParentFrame(), emp.getName());

        dialog.showDialog();

        if (dialog.isConfirmed()) {
            try {
                service.deleteEmployee(emp.getId());
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi xóa nhân viên: " + e.getMessage());
            }
        }
    }

    // =======================================================
    // STATE LISTENER
    // =======================================================
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateEmployeeList(state);
            updateBranchList(state);
            showMessages(state);
        });
    }

    @SuppressWarnings("unchecked")
    private void updateEmployeeList(AppState state) {
        Object list = state.get("Employees");
        if (list instanceof List<?> raw) {
            currentEmployees = (List<UserModel>) raw;
            view.updateTable(currentEmployees);
        }
    }

    // =======================================================
    // UPDATE BRANCHES
    // =======================================================
    private void updateBranchList(AppState state) {
        Object list = state.get("Branches");

        if (list instanceof List<?> raw) {

            branches.clear();
            List<String> names = new ArrayList<>();

            for (Object o : raw) {
                if (o instanceof BranchInfo b) {
                    Integer id = null;
                    try {
                        id = Integer.parseInt(b.getId());
                    } catch (Exception ignored) {
                    }

                    branches.add(new EmployeeFormDialog.BranchItem(id, b.getName()));
                    names.add(b.getName());
                }
            }

            if (!names.equals(lastBranchNames)) {
                view.updateBranchList(names);
                lastBranchNames = names;
            }
        }
    }

    // =======================================================
    // MESSAGE HANDLING
    // =======================================================
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
