package edu.ptithcm.utils.mapper;

import edu.ptithcm.dto.response.info_models.EmployeeInfo;
import edu.ptithcm.dto.response.info_models.InventoryInfo;
import edu.ptithcm.dto.response.info_models.InvoiceInfo;
import edu.ptithcm.dto.response.info_models.LoyaltyInfo;
import edu.ptithcm.dto.response.info_models.ProductInfo;
import edu.ptithcm.models.EmployeeModel;
import edu.ptithcm.models.InventoryModel;
import edu.ptithcm.models.InvoiceModel;
import edu.ptithcm.models.LoyaltyModel;
import edu.ptithcm.models.ProductModel;
import edu.ptithcm.models.BranchModel;
import edu.ptithcm.models.CategoryModel;
import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.dto.response.info_models.BranchInfo;
import edu.ptithcm.dto.response.info_models.CategoryInfo;
import edu.ptithcm.dto.response.info_models.CustomerInfo;

public final class MapperFactory {

    private MapperFactory() {} // chặn khởi tạo

    private static final EmployeeMapper EMPLOYEE_MAPPER = new EmployeeMapper();
    private static final ProductMapper PRODUCT_MAPPER = new ProductMapper();
    private static final CategoryMapper CATEGORY_MAPPER = new CategoryMapper();
    private static final InventoryMapper INVENTORY_MAPPER = new InventoryMapper();
    private static final BranchMapper BRANCH_MAPPER = new BranchMapper();
    private static final CustomerMapper CUSTOMER_MAPPER = new CustomerMapper();
    private static final InvoiceMapper INVOICE_MAPPER = new InvoiceMapper();
    private static final LoyaltyMapper LOYALTY_MAPPER = new LoyaltyMapper();


    public static BaseMapper<EmployeeModel, EmployeeInfo> employee() {
        return EMPLOYEE_MAPPER;
    }

    public static BaseMapper<ProductModel, ProductInfo> product() {
        return PRODUCT_MAPPER;
    }

    public static BaseMapper<CategoryModel, CategoryInfo> category() { 
        return CATEGORY_MAPPER; 
    }

    public static BaseMapper<InventoryModel, InventoryInfo> inventory() { 
        return INVENTORY_MAPPER; 
    }
    
    public static BaseMapper<BranchModel, BranchInfo> branch() {
        return BRANCH_MAPPER;
    }

    public static BaseMapper<CustomerModel, CustomerInfo> customer() {
        return CUSTOMER_MAPPER;
    }

    public static BaseMapper<InvoiceModel, InvoiceInfo> invoice() {
        return INVOICE_MAPPER;
    }

    public static BaseMapper<LoyaltyModel, LoyaltyInfo> loyalty() {
        return LOYALTY_MAPPER;
    }
}
