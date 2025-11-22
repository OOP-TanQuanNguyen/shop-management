package edu.ptithcm.views.admin.employee_dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Base dialog cho Employee form với các field chung
 */
public abstract class EmployeeFormDialog extends JDialog {

    protected JTextField txtUsername;
    protected JPasswordField txtPassword;
    protected JTextField txtName;
    protected JTextField txtPhone;
    protected JComboBox<String> cmbRole;
    protected JComboBox<BranchItem> cmbBranch;
    protected JCheckBox chkStatus;

    protected JButton btnOk;
    protected JButton btnCancel;

    protected boolean confirmed = false;

    public EmployeeFormDialog(Frame owner, String title) {
        super(owner, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(450, 500);
        setLocationRelativeTo(owner);
    }

    /**
     * Tạo form panel dùng GridBagLayout
     */
    protected JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    /**
     * Thêm một label + field vào panel theo GridBag
     */
    protected void addField(JPanel panel, String label, JComponent field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(label), gbc);

        // Field
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    /**
     * Tạo panel chứa nút OK + Cancel
     */
    protected JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnOk = new JButton("OK");
        btnCancel = new JButton("Hủy");

        btnOk.addActionListener(e -> onOkClicked());
        btnCancel.addActionListener(e -> onCancelClicked());

        panel.add(btnOk);
        panel.add(btnCancel);

        return panel;
    }

    /**
     * Sự kiện khi bấm OK - do class con xử lý
     */
    protected abstract void onOkClicked();

    protected void onCancelClicked() {
        confirmed = false;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void showDialog() {
        setVisible(true);
    }

    // ---------------------------------------------------------
    // ROLE COMBOBOX
    // ---------------------------------------------------------
    protected void initRoleComboBox() {
        cmbRole = new JComboBox<>(new String[]{"ADMIN", "STAFF"});
        cmbRole.setSelectedIndex(1);
    }

    // ---------------------------------------------------------
    // BRANCH COMBOBOX
    // ---------------------------------------------------------
    protected void initBranchComboBox(java.util.List<BranchItem> branches) {
        cmbBranch = new JComboBox<>();
        cmbBranch.addItem(new BranchItem(null, "-- Chọn chi nhánh --"));

        if (branches != null) {
            for (BranchItem b : branches) {
                cmbBranch.addItem(b);
            }
        }
    }

    /**
     * Object đại diện cho từng chi nhánh trong dropdown
     */
    public static class BranchItem {

        private final Integer id;
        private final String name;

        public BranchItem(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name; // hiển thị tên chi nhánh trong combo
        }
    }
}
