package edu.ptithcm.views.admin.product_dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.sql.Date;

public class ProductEditDialog extends ProductFormDialog {

    private final String productId;

    public ProductEditDialog(Frame owner, String id, String name, String category,
            Double costPrice, Double sellPrice, String expiry, boolean status) {
        super(owner, "✏️ Cập nhật sản phẩm");
        this.productId = id;
        initComponents(name, category, costPrice, sellPrice, expiry, status);

        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents(String name, String category, Double costPrice,
            Double sellPrice, String expiry, boolean status) {
        JPanel formPanel = createFormPanel();

        txtName = new JTextField(name != null ? name : "", 20);
        txtCategoryId = new JTextField(category != null ? category : "", 20);
        txtCostPrice = new JTextField(costPrice != null ? String.valueOf(costPrice) : "", 20);
        txtSellPrice = new JTextField(sellPrice != null ? String.valueOf(sellPrice) : "", 20);
        txtExpiry = new JTextField(expiry != null ? expiry : "", 20);
        chkStatus = new JCheckBox("Đang bán", status);

        addField(formPanel, "Tên sản phẩm: *", txtName, 0);
        addField(formPanel, "Mã danh mục:", txtCategoryId, 1);
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
                JOptionPane.showMessageDialog(this, "Giá bán phải lớn hơn hoặc bằng giá vốn!",
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
                        "Định dạng Hạn sử dụng không hợp lệ (Phải là yyyy-MM-dd)!",
                        "Lỗi", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        return true;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();

        data.put("name", getProductName());
        data.put("categoryId", getCategoryId());
        data.put("costPrice", getCostPrice());
        data.put("sellPrice", getSellPrice());

        String expiry = getExpiryDate();
        if (expiry != null && !expiry.isEmpty()) {
            try {
                data.put("expiryDate", (expiry == null || expiry.isEmpty()) ? null : expiry);
            } catch (IllegalArgumentException e) {
                data.put("expiryDate", null);
            }
        } else {
            data.put("expiryDate", null);
        }

        data.put("isActive", getStatus());

        return data;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return txtName.getText().trim();
    }

    public String getCategoryId() {
        String val = txtCategoryId.getText().trim();
        return val.isEmpty() ? null : val;
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
