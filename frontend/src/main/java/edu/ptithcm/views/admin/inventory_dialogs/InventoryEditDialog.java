package edu.ptithcm.views.admin.inventory_dialogs;

import javax.swing.*;
import java.awt.*;

public class InventoryEditDialog extends InventoryFormDialog {

    private final Integer inventoryId;

    public InventoryEditDialog(Frame owner,
            Integer id,
            String branchName,
            String productName,
            Integer quantity) {

        super(owner, "Cập nhật kho");

        this.inventoryId = id;

        initComponents(branchName, productName, quantity);

        // ===== FIX: đưa dialog ra giữa màn hình =====
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents(String branchName,
            String productName,
            Integer quantity) {

        JPanel form = createFormPanel();

        cbBranch = new JComboBox<>();
        cbProduct = new JComboBox<>();
        txtQuantity = new JTextField(String.valueOf(quantity), 20);

        // Không cho edit branch + product
        cbBranch.addItem(branchName);
        cbProduct.addItem(productName);

        cbBranch.setEnabled(false);
        cbProduct.setEnabled(false);

        addField(form, "Chi nhánh:", cbBranch, 0);
        addField(form, "Sản phẩm:", cbProduct, 1);
        addField(form, "Số lượng:", txtQuantity, 2);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);

        // ===== FIX: nút bấm đẹp và căn phải =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(createButtonPanel());
        add(btnPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void onOkClicked() {
        try {
            Integer.parseInt(txtQuantity.getText().trim());
            confirmed = true;
            setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Số lượng phải là số hợp lệ",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public Integer getQuantity() {
        return Integer.valueOf(txtQuantity.getText().trim());
    }
}
