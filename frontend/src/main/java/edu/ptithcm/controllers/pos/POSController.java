package edu.ptithcm.controllers.pos;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;

import edu.ptithcm.protocols.DTTP;

import edu.ptithcm.services.pos.POSServices;
import edu.ptithcm.services.pos.InvoiceService;

import edu.ptithcm.services.admin.ProductService;
import edu.ptithcm.services.admin.CustomerService;
import edu.ptithcm.services.admin.InventoryService;
import edu.ptithcm.services.admin.LoyaltyService;   // ← THÊM

import edu.ptithcm.views.pos.POSForm;
import edu.ptithcm.views.pos.panels.SalePanel;
import edu.ptithcm.views.pos.panels.MyInvoicePanel;

public class POSController {

    private final POSForm view;
    private final DTTP client;
    private final Store store = Store.getInstance();

    private ProductService productService;
    private InventoryService inventoryService;
    private InvoiceService invoiceService;
    private CustomerService customerService;
    private LoyaltyService loyaltyService;   // ← THÊM

    public POSController(POSForm view, DTTP client) {
        this.view = view;
        this.client = client;

        registerEvent();

        // Tạo service 1 lần – tránh lỗi state/event
        initServices();

        // Module POS
        initSaleModule();
        initMyInvoiceModule();

        store.subcribe(this::handleState);
    }

    private void registerEvent() {
        view.getLogoutButton().addActionListener(e
                -> POSServices.handleLogout(this.view)
        );
    }

    /* =================================================================
     * INIT SERVICES (khởi tạo 1 lần duy nhất)
     * ================================================================= */
    private void initServices() {
        productService = new ProductService(client);
        inventoryService = new InventoryService(client);
        invoiceService = new InvoiceService(client);
        customerService = new CustomerService(client);
        loyaltyService = new LoyaltyService(client);   // ← THÊM
    }

    /* =================================================================
     * SALE MODULE
     * ================================================================= */
    private void initSaleModule() {
        try {
            SalePanel salePanel = view.getSalePanel();

            // Gọi bản SaleController 6 parameters — KHÔNG ĐỔI CẤU TRÚC
            new SaleController(
                    salePanel,
                    productService,
                    inventoryService,
                    invoiceService,
                    customerService,
                    loyaltyService // ← THÊM
            );

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init SaleController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* =================================================================
     * MY INVOICE MODULE
     * ================================================================= */
    private void initMyInvoiceModule() {
        try {
            MyInvoicePanel invoicePanel = view.getMyInvoicePanel();

            new InvoiceController(invoicePanel, invoiceService);

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init InvoiceController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleState(AppState state) {
        // Nếu cần xử lý state chung của POS thì thêm tại đây
    }
}
