package edu.ptithcm.views.admin.inventory_dialogs;

import edu.ptithcm.models.BranchInfo;
import edu.ptithcm.models.ProductInfo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InventoryAddDialog extends InventoryFormDialog {

    private final List<BranchInfo> branches;
    private final List<ProductInfo> products;

    public InventoryAddDialog(Frame owner, List<BranchInfo> branches, List<ProductInfo> products) {
        super(owner, "➕ Thêm kho mới");
        this.branches = branches;
        this.products = products;

        initComponents();
    }

    private void initComponents() {
        JPanel form = createFormPanel();

        // Combo branch
        cbBranch = new JComboBox<>();
        for (BranchInfo b : branches) {
            cbBranch.addItem(b.getName());
        }

        // Combo product
        cbProduct = new JComboBox<>();
        for (ProductInfo p : products) {
            cbProduct.addItem(p.getName());
        }

        // Quantity
        txtQuantity = new JTextField("0", 15);

        addField(form, "Chi nhánh:", cbBranch, 0);
        addField(form, "Sản phẩm:", cbProduct, 1);
        addField(form, "Số lượng:", txtQuantity, 2);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    @Override
    protected void onOkClicked() {
        if (!validateInput()) {
            return;
        }
        confirmed = true;
        setVisible(false);
    }

    private boolean validateInput() {
        try {
            Integer.parseInt(txtQuantity.getText().trim());
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Số lượng phải là số nguyên!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String getBranchId() {
        return branches.get(cbBranch.getSelectedIndex()).getId();
    }

    public String getProductId() {
        return products.get(cbProduct.getSelectedIndex()).getId();
    }

    public Integer getQuantity() {
        return Integer.valueOf(txtQuantity.getText().trim());
    }

    public void showDialog() {
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }
}
