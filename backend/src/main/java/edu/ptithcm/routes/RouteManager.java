package edu.ptithcm.routes;

import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.routes.authentication.LoginRoute;
import edu.ptithcm.routes.employee.EmployeeRoute;
import edu.ptithcm.routes.product.ProductRoute;
import edu.ptithcm.routes.category.CategoryRoute;
import edu.ptithcm.routes.invoice.InvoiceRoute;
import edu.ptithcm.routes.inventory.InventoryRoute;
import edu.ptithcm.routes.branch.BranchRoute;
import edu.ptithcm.routes.customer.CustomerRoute;
import edu.ptithcm.routes.loyalty.LoyaltyRoute;
import edu.ptithcm.routes.shift.ShiftRoute;
import edu.ptithcm.routes.shiftassignment.ShiftAssignmentRoute;

public class RouteManager {

    private final DTTP server;
    private final DTTPStateManager manager;

    public RouteManager(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
    }

    public void registerRoutes() {
        // Gọi đăng ký từng route
        new LoginRoute(server, manager).register();
        new EmployeeRoute(server, manager).register();
        new ProductRoute(server, manager).register();
        new CategoryRoute(server, manager).register();
        new InvoiceRoute(server, manager).register();
        new InventoryRoute(server, manager).register();
        new BranchRoute(server, manager).register();
        new CustomerRoute(server, manager).register();
        new LoyaltyRoute(server, manager).register();
        new ShiftRoute(server, manager).register();
        new ShiftAssignmentRoute(server, manager).register();
    }
}
