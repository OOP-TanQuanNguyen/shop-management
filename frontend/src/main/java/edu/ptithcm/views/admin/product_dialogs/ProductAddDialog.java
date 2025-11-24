package edu.ptithcm.views.admin.product_dialogs;

import edu.ptithcm.models.CategoryModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;

public class ProductAddDialog extends ProductFormDialog {

    private JComboBox<String> cbCategory;
    private List<CategoryModel> categories;

    public ProductAddDialog(Frame owner, List<CategoryModel> categories) {
        super(owner, "➕ Thêm sản phẩm mới");

        this.categories = categories;

        initComponents();

        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel formPanel = createFormPanel();

        txtName = new JTextField(20);

        // ============================
        // COMBOBOX DANH MỤC
        // ============================
        cbCategory = new JComboBox<>();
        categories.forEach(c -> cbCategory.addItem(c.getName()));

        txtCostPrice = new JTextField(20);
        txtSellPrice = new JTextField(20);
        txtExpiry = new JTextField(20);
        chkStatus = new JCheckBox("Đang bán", true);

        addField(formPanel, "Tên sản phẩm: *", txtName, 0);
        addField(formPanel, "Danh mục:", cbCategory, 1);   // COMBOBOX
        addField(formPanel, "Giá vốn: *", txtCostPrice, 2);
        addField(formPanel, "Giá bán: *", txtSellPrice, 3);
        addField(formPanel, "Hạn sử dụng (yyyy-MM-dd):", txtExpiry, 4);
        addField(formPanel, "Trạng thái:", chkStatus, 5);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    @Override
    protected void onOkClicked() {
        if (validateInput()) {
            confirmed = true;
            setVisible(false);
        }
    }

    // VALIDATION GIỮ NGUYÊN
    private boolean validateInput() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            double cost = Double.parseDouble(txtCostPrice.getText().trim());
            if (cost <= 0) {
                JOptionPane.showMessageDialog(this, "Giá vốn phải lớn hơn 0!",
                        "Lỗi", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá vốn không hợp lệ!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            double sell = Double.parseDouble(txtSellPrice.getText().trim());
            double cost = Double.parseDouble(txtCostPrice.getText().trim());
            if (sell < cost) {
                JOptionPane.showMessageDialog(this, "Giá bán phải >= giá vốn!",
                        "Lỗi", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá bán không hợp lệ!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String expiry = getExpiryDate();
        if (expiry != null && !expiry.isEmpty()) {
            try {
                Date.valueOf(expiry);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this,
                        "Hạn sử dụng không hợp lệ (yyyy-MM-dd)!",
                        "Lỗi", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        return true;
    }

    // ============================
    // TẠO MAP GỬI VỀ CONTROLLER
    // ============================
    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();

        data.put("name", getProductName());
        data.put("categoryId", getCategoryId());   // LẤY CATEGORY ID
        data.put("costPrice", getCostPrice());
        data.put("sellPrice", getSellPrice());

        String expiry = getExpiryDate();
        data.put("expiryDate", (expiry == null || expiry.isEmpty()) ? null : expiry);

        data.put("isActive", getStatus());

        return data;
    }

    // ============================
    // GETTERS
    // ============================
    public String getProductName() {
        return txtName.getText().trim();
    }

    // LẤY ID TỪ DANH SÁCH CATEGORY
    public String getCategoryId() {
        int index = cbCategory.getSelectedIndex();
        if (index < 0) {
            return null;
        }
        return categories.get(index).getCategoryId();
    }

    public Double getCostPrice() {
        try {
            return Double.parseDouble(txtCostPrice.getText().trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public Double getSellPrice() {
        try {
            return Double.parseDouble(txtSellPrice.getText().trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public String getExpiryDate() {
        String val = txtExpiry.getText().trim();
        return val.isEmpty() ? null : val;
    }

    public Boolean getStatus() {
        return chkStatus.isSelected();
    }
}
