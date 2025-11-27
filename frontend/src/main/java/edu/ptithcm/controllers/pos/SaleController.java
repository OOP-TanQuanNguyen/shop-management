package edu.ptithcm.controllers.pos;

import java.io.IOException;
import java.util.*;
import java.awt.Window;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Timer;
import java.math.BigDecimal;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;

import edu.ptithcm.models.*;
import edu.ptithcm.services.admin.InventoryService;
import edu.ptithcm.services.admin.ProductService;
import edu.ptithcm.services.pos.InvoiceService;
import edu.ptithcm.services.admin.CustomerService;

import edu.ptithcm.views.pos.panels.SalePanel;
import edu.ptithcm.views.pos.panels.customer_dialogs.CustomerSelectDialog;

public class SaleController {

    private final SalePanel view;
    private final Store store = Store.getInstance();

    private final ProductService productService;
    private final InventoryService inventoryService;
    private final InvoiceService invoiceService;
    private final CustomerService customerService;

    private List<ProductInfo> products = new ArrayList<>();
    private final Map<String, InventoryModel> inventoryMap = new HashMap<>();

    private final Map<String, CartItem> cart = new LinkedHashMap<>();

    private String currentDraftId = null;
    private CustomerModel selectedCustomer = null;

    // ✅ FIX: Dùng Timer để debounce draft updates
    private Timer draftUpdateTimer = null;
    private static final long DRAFT_UPDATE_DELAY = 300;

    /* ------------------------ Cart Item ------------------------ */
    private static class CartItem {

        ProductInfo product;
        int quantity;

        CartItem(ProductInfo p, int q) {
            product = p;
            quantity = q;
        }
    }

    /* ======================================================================================= */
    public SaleController(
            SalePanel view,
            ProductService productService,
            InventoryService inventoryService,
            InvoiceService invoiceService,
            CustomerService customerService
    ) {
        this.view = view;
        this.productService = productService;
        this.inventoryService = inventoryService;
        this.invoiceService = invoiceService;
        this.customerService = customerService;

        registerEvents();
        store.subcribe(this::onStateChanged);
        loadInitialData();
    }

    /* ======================================================================================= */
    private void loadInitialData() {
        try {
            // 1. Load danh sách sản phẩm
            productService.getAllProducts();

            // 2. Lấy user để biết chi nhánh
            UserModel user = (UserModel) store.getAppState().get("user");
            if (user != null) {
                inventoryService.getInventoriesByBranch(user.getBranchId());
            }

            // 3. Load danh sách khách hàng
            customerService.getAllCustomers();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Không thể tải dữ liệu ban đầu:\n" + e.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* ======================================================================================= */
    private void registerEvents() {

        view.setSearchListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterProducts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterProducts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterProducts();
            }
        });

        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnRemove().addActionListener(e -> handleRemove());
        view.getBtnPay().addActionListener(e -> handlePay());
        view.getBtnSelectCustomer().addActionListener(e -> selectCustomer());
    }

    /* ======================================================================================= */
    @SuppressWarnings("unchecked")
    private void onStateChanged(AppState state) {

        SwingUtilities.invokeLater(() -> {

            Object p = state.get("Products");
            if (p instanceof List<?> list) {
                products = (List<ProductInfo>) list;
                filterProducts();
            }

            Object inv = state.get("Inventories");
            if (inv instanceof List<?> list) {
                inventoryMap.clear();
                for (Object o : list) {
                    InventoryModel m = (InventoryModel) o;
                    inventoryMap.put(m.getProductId(), m);
                }
                filterProducts();
            }

            Object invoice = state.get("Invoice");
            if (invoice instanceof InvoiceInfo info) {
                currentDraftId = info.getInvoiceId();
            }
        });
    }

    /* ======================================================================================= */
    private void filterProducts() {

        String keyword = view.getTxtSearch().getText().trim().toLowerCase();
        List<Object[]> rows = new ArrayList<>();

        for (ProductInfo p : products) {
            int qty = inventoryMap.containsKey(p.getId())
                    ? inventoryMap.get(p.getId()).getQuantity()
                    : 0;

            if (keyword.isEmpty() || p.getName().toLowerCase().contains(keyword)) {
                rows.add(new Object[]{
                    p.getId(), p.getName(), p.getSellPrice(), qty
                });
            }
        }

        view.updateProductTable(rows);
    }

    /* ======================================================================================= */
    private void handleAdd() {

        int row = view.getProductTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Chọn sản phẩm!");
            return;
        }

        String productId = (String) view.getProductTable().getValueAt(row, 0);
        ProductInfo prod = findProduct(productId);
        InventoryModel inv = inventoryMap.get(productId);

        if (prod == null || inv == null) {
            JOptionPane.showMessageDialog(view, "Không thể lấy thông tin sản phẩm");
            return;
        }

        cart.compute(productId, (id, item) -> {
            if (item == null) {
                return new CartItem(prod, 1);
            }

            if (item.quantity + 1 > inv.getQuantity()) {
                JOptionPane.showMessageDialog(view, "Không đủ tồn kho");
                return item;
            }

            item.quantity++;
            return item;
        });

        refreshCart();
        scheduleDraftUpdate();
    }

    /* ======================================================================================= */
    private void handleRemove() {

        int row = view.getCartTable().getSelectedRow();
        if (row == -1) {
            return;
        }

        String productId = (String) view.getCartTable().getValueAt(row, 0);
        cart.remove(productId);

        refreshCart();
        scheduleDraftUpdate();
    }

    /* ======================================================================================= */
    private void refreshCart() {

        List<Object[]> rows = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.values()) {

            BigDecimal unit = BigDecimal.valueOf(item.product.getSellPrice());
            BigDecimal line = unit.multiply(BigDecimal.valueOf(item.quantity));

            rows.add(new Object[]{
                item.product.getId(),
                item.product.getName(),
                item.quantity,
                unit,
                line
            });

            total = total.add(line);
        }

        view.updateCartTable(rows);
        view.updateTotal("Tổng: " + total + " ₫");
    }

    /* ======================================================================================= */
    private void selectCustomer() {

        CustomerModel chosen = showDialogSelectCustomer();
        if (chosen == null) {
            return;
        }

        selectedCustomer = chosen;
        view.getLblCustomerName().setText(
                "Khách hàng: " + chosen.getName() + " (" + chosen.getPhone() + ")"
        );

        scheduleDraftUpdate();
    }

    /* ======================================================================================= */
    @SuppressWarnings("unchecked")
    private CustomerModel showDialogSelectCustomer() {

        Window w = SwingUtilities.getWindowAncestor(view);
        CustomerSelectDialog dlg = new CustomerSelectDialog(w);
        dlg.setVisible(true);

        CustomerModel input = dlg.getResult();
        if (input == null) {
            return null;
        }

        List<CustomerModel> all = (List<CustomerModel>) store.getAppState().get("Customers");

        if (all != null) {
            for (CustomerModel c : all) {
                if (c.getPhone().equals(input.getPhone())) {
                    return c;
                }
            }
        }

        try {
            customerService.createCustomer(input.getName(), input.getPhone(), 0);
            customerService.getAllCustomers();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Không thể tạo khách hàng:\n" + e.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }

        return input;
    }

    /* ======================================================================================= */
    private Map<String, Object> buildPayload() {

        UserModel user = (UserModel) store.getAppState().get("user");

        List<Map<String, Object>> details = new ArrayList<>();
        for (CartItem item : cart.values()) {
            details.add(Map.of(
                    "productId", item.product.getId(),
                    "quantity", item.quantity
            ));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("details", details);
        map.put("employeeId", user.getId());
        map.put("branchId", String.valueOf(user.getBranchId()));

        if (selectedCustomer != null) {
            map.put("customerId", selectedCustomer.getId());
        }

        return map;
    }

    /* ======================================================================================= */
    // ✅ FIX: Dùng Timer để debounce thay vì throttle thủ công
    private void scheduleDraftUpdate() {

        if (draftUpdateTimer != null) {
            draftUpdateTimer.cancel();
        }

        draftUpdateTimer = new Timer();
        draftUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> updateDraft());
            }
        }, DRAFT_UPDATE_DELAY);
    }

    /* ======================================================================================= */
    private void updateDraft() {

        if (cart.isEmpty()) {
            currentDraftId = null;
            return;
        }

        Map<String, Object> payload = buildPayload();

        try {
            if (currentDraftId == null) {
                invoiceService.createInvoice(payload);
            } else {
                payload.put("invoiceId", currentDraftId);
                invoiceService.updateInvoice(payload);
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view,
                    "Không thể gửi yêu cầu lên server:\n" + ex.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi cập nhật hóa đơn:\n" + ex.getMessage());
        }
    }

    /* ======================================================================================= */
    // ✅ FIX: Gọi confirmInvoice() thay vì chỉ chuyển tab
    // ====================== HANDLE PAY ===========================
    private void handlePay() {

        if (currentDraftId == null) {
            JOptionPane.showMessageDialog(view, "Chưa có hóa đơn draft!");
            return;
        }

        // Xác nhận trước khi thanh toán
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Xác nhận thanh toán hóa đơn?\n"
                + "Mã HĐ: " + currentDraftId + "\n"
                + "Tổng tiền: " + view.getLblTotal().getText(),
                "Xác nhận thanh toán",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // === Gửi yêu cầu xác nhận hóa đơn ===
            invoiceService.confirmInvoice(currentDraftId);

            JOptionPane.showMessageDialog(
                    view,
                    "Thanh toán thành công!\n"
                    + "Mã hóa đơn: " + currentDraftId,
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // ==== Reset sale panel ====
            cart.clear();
            currentDraftId = null;
            selectedCustomer = null;

            view.getLblCustomerName().setText("Khách hàng: Chưa chọn");
            refreshCart();

            // ==== Load lại danh sách hóa đơn của nhân viên ====
            invoiceService.getInvoicesForEmployee();

            // ==== CHUYỂN SANG TAB "Hóa đơn của tôi" ====
            SwingUtilities.invokeLater(() -> {
                // Lấy POSForm cha
                Window win = SwingUtilities.getWindowAncestor(view);
                if (win instanceof edu.ptithcm.views.pos.POSForm posForm) {
                    posForm.switchToMyInvoiceTab();
                }
            });

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Không thể xác nhận thanh toán:\n" + ex.getMessage(),
                    "Lỗi kết nối",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Lỗi xác nhận thanh toán:\n" + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    /* ======================================================================================= */
    private ProductInfo findProduct(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
