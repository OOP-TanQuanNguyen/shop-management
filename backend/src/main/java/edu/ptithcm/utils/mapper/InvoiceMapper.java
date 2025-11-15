package edu.ptithcm.utils.mapper;

import edu.ptithcm.models.InvoiceDetailModel;
import edu.ptithcm.models.InvoiceModel;
import edu.ptithcm.dto.response.info_models.InvoiceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceMapper implements BaseMapper<InvoiceModel, InvoiceInfo> {

    @Override
    public InvoiceInfo toDTO(InvoiceModel entity) {
        if (entity == null) return null;

        List<Map<String, Object>> detailList = new ArrayList<>();
        if (entity.getDetails() != null) {
            for (InvoiceDetailModel d : entity.getDetails()) {
                Map<String, Object> dm = new HashMap<>();
                dm.put("productId", d.getProduct() != null ? d.getProduct().getId() : null);
                dm.put("quantity", d.getQuantity());
                dm.put("unitPrice", d.getUnitPrice());
                dm.put("total", d.getTotal());
                detailList.add(dm);
            }
        }

        return new InvoiceInfo.Builder()
                .invoiceId(entity.getId())
                .employeeId(entity.getEmployee() != null ? entity.getEmployee().getId() : null)
                .branchId(entity.getBranch() != null ? String.valueOf(entity.getBranch().getId()) : null)
                .customerId(entity.getCustomer() != null ? entity.getCustomer().getId() : null)
                .createdAt(entity.getCreatedAt())
                .total(entity.getTotal())
                .discount(entity.getDiscount())
                .note(entity.getNote())
                .details(detailList)
                .build();
    }

    @Override
    public List<InvoiceInfo> toDTOList(List<InvoiceModel> entities) {
        List<InvoiceInfo> list = new ArrayList<>();
        if (entities != null) {
            for (InvoiceModel e : entities) list.add(toDTO(e));
        }
        return list;
    }
}
