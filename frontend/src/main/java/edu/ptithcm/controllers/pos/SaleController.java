package edu.ptithcm.controllers.pos;

import java.io.IOException;
import java.util.*;
import java.awt.Window;
import java.util.concurrent.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

    private final ScheduledExecutorService scheduler
            = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> draftFuture;
    private static final long DRAFT_UPDATE_DELAY = 250;

    /* ------------------------ Cart Item ------------------------ */
    private static class CartItem {

        ProductInfo product;
        int quantity;

        CartItem(ProductInfo p, int q) {
            product = p;
            quantity = q;
        }
    }

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

    private void loadInitialData() {
        try {
            productService.getAllProducts();

            UserModel user = (UserModel) store.getAppState().get("user");
            if (user != null) {
                inventoryService.getInventoriesByBranch(user.getBranchId());
            }

            customerService.getAllCustomers();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Không thể tải dữ liệu ban đầu:\n" + e.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }

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

            // CHẶN message update draft từ reducer
            state.set("InvoiceMessage", "");
            state.set("InvoiceError", "");
        });
    }

    private void filterProducts() {

        String keyword = view.getTxtSearch().getText().trim().toLowerCase();
        List<Object[]> rows = new ArrayList<>();

        for (ProductInfo p : products) {

            InventoryModel inv = inventoryMap.get(p.getId());
            int qty = (inv != null && inv.getQuantity() != null)
                    ? inv.getQuantity()
                    : 0;

            if (keyword.isEmpty() || p.getName().toLowerCase().contains(keyword)) {
                rows.add(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getSellPrice(),
                    qty
                });
            }
        }

        view.updateProductTable(rows);
    }

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

    /* ============================================================
       ✅ FIX: ĐỢI STORE CẬP NHẬT SAU KHI TẠO KHÁCH HÀNG MỚI
    ============================================================ */
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

        // Kiểm tra khách đã tồn tại
        if (all != null) {
            for (CustomerModel c : all) {
                if (c.getPhone().equals(input.getPhone())) {
                    return c;  // ✅ Khách đã có trong danh sách
                }
            }
        }

        // ===== TẠO KHÁCH HÀNG MỚI =====
        try {
            customerService.createCustomer(input.getName(), input.getPhone(), 0);

            // ✅ ĐỢI STORE CẬP NHẬT (polling 5 giây)
            CustomerModel newCustomer = waitForCustomerInStore(input.getPhone(), 5000);

            if (newCustomer != null) {
                return newCustomer;  // ✅ Trả về customer có ID từ DB
            } else {
                JOptionPane.showMessageDialog(view,
                        "Không thể tải thông tin khách hàng mới.\nVui lòng chọn lại từ danh sách.",
                        "Timeout", JOptionPane.WARNING_MESSAGE);
                return null;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Không thể tạo khách hàng:\n" + e.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /* ============================================================
       ✅ THÊM METHOD ĐỢI STORE CẬP NHẬT
    ============================================================ */
    @SuppressWarnings("unchecked")
    private CustomerModel waitForCustomerInStore(String phone, long timeoutMs) {

        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeoutMs) {

            List<CustomerModel> customers
                    = (List<CustomerModel>) store.getAppState().get("Customers");

            if (customers != null) {
                for (CustomerModel c : customers) {
                    if (c.getPhone().equals(phone) && c.getId() != null) {
                        return c;  // ✅ Tìm thấy khách hàng có ID
                    }
                }
            }

            // Chờ 100ms rồi thử lại
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        return null;  // Timeout
    }

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
        map.put("branchId", user.getBranchId());
        map.put("discount", BigDecimal.ZERO);
        map.put("note", "");

        if (selectedCustomer != null) {
            map.put("customerId", selectedCustomer.getId());
        }

        if (currentDraftId != null) {
            map.put("invoiceId", currentDraftId);
        }

        return map;
    }

    private void scheduleDraftUpdate() {

        if (draftFuture != null && !draftFuture.isDone()) {
            draftFuture.cancel(false);
        }

        draftFuture = scheduler.schedule(() -> {
            SwingUtilities.invokeLater(this::updateDraft);
        }, DRAFT_UPDATE_DELAY, TimeUnit.MILLISECONDS);
    }

    private Map<String, Object> lastPayload = null;

    private void updateDraft() {

        Map<String, Object> payload = buildPayload();

        if (payload.equals(lastPayload)) {
            return;
        }
        lastPayload = new HashMap<>(payload);

        try {
            if (currentDraftId == null) {
                invoiceService.createInvoice(payload);
            } else {
                invoiceService.updateInvoice(payload);
            }
        } catch (Exception ignore) {
            // Không show popup khi update draft
        }
    }

    private void handlePay() {

        if (currentDraftId == null) {
            JOptionPane.showMessageDialog(view, "Chưa có hóa đơn draft!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Xác nhận thanh toán hóa đơn?",
                "Thanh toán",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            invoiceService.confirmInvoice(currentDraftId);

            JOptionPane.showMessageDialog(view, "Thanh toán thành công!");

            cart.clear();
            currentDraftId = null;
            selectedCustomer = null;
            view.getLblCustomerName().setText("Khách hàng: Chưa chọn");
            refreshCart();

            invoiceService.getInvoicesForEmployee();

            SwingUtilities.invokeLater(() -> {
                Window win = SwingUtilities.getWindowAncestor(view);
                if (win instanceof edu.ptithcm.views.pos.POSForm posForm) {
                    posForm.switchToMyInvoiceTab();
                }
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi thanh toán:\n" + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ProductInfo findProduct(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
