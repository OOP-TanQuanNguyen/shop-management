package edu.ptithcm.views.pos.panels.customer_dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.app.store.Store;

public class CustomerSelectDialog extends JDialog {

    private JTextField txtSearch;
    private JList<String> listCustomers;
    private DefaultListModel<String> listModel;

    private JTextField txtName;
    private JTextField txtPhone;

    private CustomerModel result = null;
    private List<CustomerModel> allCustomers;

    public CustomerSelectDialog(Window parent) {
        super(parent, "Thêm / Chọn khách hàng", ModalityType.APPLICATION_MODAL);
        initUI();
        loadCustomers();
    }

    private void initUI() {

        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(500, 400));

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("🔍 Tìm khách hàng có sẵn"));

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterCustomers();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterCustomers();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterCustomers();
            }
        });

        listModel = new DefaultListModel<>();
        listCustomers = new JList<>(listModel);
        listCustomers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listCustomers.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(listCustomers);
        scrollPane.setPreferredSize(new Dimension(450, 150));

        topPanel.add(txtSearch, BorderLayout.NORTH);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 6, 6));
        centerPanel.setBorder(BorderFactory.createTitledBorder(" Hoặc thêm khách hàng mới"));

        centerPanel.add(new JLabel("Tên khách hàng:"));
        txtName = new JTextField();
        centerPanel.add(txtName);

        centerPanel.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        centerPanel.add(txtPhone);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnSelect = new JButton(" Chọn");
        JButton btnAddNew = new JButton(" Thêm mới");
        JButton btnCancel = new JButton(" Hủy");

        btnSelect.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnAddNew.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        buttons.add(btnSelect);
        buttons.add(btnAddNew);
        buttons.add(btnCancel);
        add(buttons, BorderLayout.SOUTH);

        btnSelect.addActionListener(e -> onSelectExisting());
        btnAddNew.addActionListener(e -> onAddNew());
        btnCancel.addActionListener(e -> {
            result = null;
            dispose();
        });

        listCustomers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    onSelectExisting();
                }
            }
        });

        pack();
        setResizable(false);
        setLocationRelativeTo(getParent());
    }

    @SuppressWarnings("unchecked")
    private void loadCustomers() {
        Store store = Store.getInstance();
        Object obj = store.getAppState().get("Customers");

        if (obj instanceof List<?>) {
            allCustomers = (List<CustomerModel>) obj;
            filterCustomers();
        }
    }

    private void filterCustomers() {
        if (allCustomers == null) {
            return;
        }

        String keyword = txtSearch.getText().trim().toLowerCase();
        listModel.clear();

        for (CustomerModel c : allCustomers) {
            String name = c.getName() != null ? c.getName().toLowerCase() : "";
            String phone = c.getPhone() != null ? c.getPhone() : "";

            if (keyword.isEmpty() || name.contains(keyword) || phone.contains(keyword)) {
                listModel.addElement(c.getName() + " - " + c.getPhone());
            }
        }
    }

    private void onSelectExisting() {
        int index = listCustomers.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng từ danh sách!");
            return;
        }

        String selected = listCustomers.getSelectedValue();
        if (allCustomers == null) {
            return;
        }

        for (CustomerModel c : allCustomers) {
            String display = c.getName() + " - " + c.getPhone();
            if (display.equals(selected)) {
                result = c;
                break;
            }
        }

        dispose();
    }

    private void onAddNew() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên không được để trống!");
            return;
        }

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "SĐT không được để trống!");
            return;
        }

        // ============================================================
        // VALIDATE SĐT 9–11 SỐ (giống Employee)
        // ============================================================
        String cleaned = phone.replaceAll("[^0-9]", "");
        if (cleaned.length() < 9 || cleaned.length() > 11) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại phải từ 9 đến 11 số!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // CHECK TRÙNG SĐT
        if (allCustomers != null) {
            for (CustomerModel c : allCustomers) {
                if (c.getPhone().equals(phone)) {
                    int choice = JOptionPane.showConfirmDialog(
                            this,
                            "Số điện thoại này đã tồn tại:\n" + c.getName() + " - " + c.getPhone()
                            + "\n\nChọn khách hàng này?",
                            "Khách hàng đã tồn tại",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (choice == JOptionPane.YES_OPTION) {
                        result = c;
                        dispose();
                    }
                    return;
                }
            }
        }

        result = new CustomerModel.Builder()
                .name(name)
                .phone(phone)
                .point(0)
                .build();

        dispose();
    }

    public CustomerModel getResult() {
        return result;
    }
}
