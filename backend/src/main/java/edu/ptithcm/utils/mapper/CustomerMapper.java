package edu.ptithcm.utils.mapper;

import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.dto.response.info_models.CustomerInfo;

public class CustomerMapper implements BaseMapper<CustomerModel, CustomerInfo> {

    @Override
    public CustomerInfo toDTO(CustomerModel entity) {
        if (entity == null) return null;

        return new CustomerInfo.Builder()
                .customerId(entity.getId())
                .name(entity.getName())
                .phone(entity.getPhone())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
