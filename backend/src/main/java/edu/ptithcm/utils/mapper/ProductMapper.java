package edu.ptithcm.utils.mapper;

import java.util.Collections;
import java.util.List;

import edu.ptithcm.dto.response.info_models.ProductInfo;
import edu.ptithcm.models.ProductModel;

final class ProductMapper implements BaseMapper<ProductModel, ProductInfo> {

    ProductMapper() {
    }

    @Override
    public ProductInfo toDTO(ProductModel p) {
        if (p == null) {
            return null;
        }

        return new ProductInfo.Builder()
                .productId(p.getId())
                .name(p.getName())
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                // FIX QUAN TRỌNG
                .costPrice(p.getCostPrice())
                .sellPrice(p.getSellPrice())
                .expiryDate(p.getExpiryDate())
                .isActive(p.isActive())
                .build();
    }

    @Override
    public List<ProductInfo> toDTOList(List<ProductModel> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(this::toDTO).toList();
    }
}
