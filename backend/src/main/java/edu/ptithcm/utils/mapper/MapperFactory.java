package edu.ptithcm.utils.mapper;

import edu.ptithcm.dto.response.info_models.EmployeeInfo;
import edu.ptithcm.dto.response.info_models.ProductInfo;
import edu.ptithcm.models.EmployeeModel;
import edu.ptithcm.models.ProductModel;

public final class MapperFactory {

    private MapperFactory() {} // chặn khởi tạo

    private static final EmployeeMapper EMPLOYEE_MAPPER = new EmployeeMapper();
    private static final ProductMapper PRODUCT_MAPPER = new ProductMapper();

    public static BaseMapper<EmployeeModel, EmployeeInfo> employee() {
        return EMPLOYEE_MAPPER;
    }

    public static BaseMapper<ProductModel, ProductInfo> product() {
        return PRODUCT_MAPPER;
    }
}
