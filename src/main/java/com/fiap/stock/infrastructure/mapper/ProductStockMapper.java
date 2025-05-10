package com.fiap.stock.infrastructure.mapper;

import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.infrastructure.repository.model.ProductStockModel;

public abstract class ProductStockMapper {
    private ProductStockMapper() {
    }

    public static ProductStock toEntity(ProductStockModel productStockModel) {
        return new ProductStock(
                productStockModel.getId(),
                productStockModel.getSku(),
                productStockModel.getName(),
                productStockModel.getQuantity()
        );
    }

    public static ProductStockModel toModel(ProductStock productStock) {
        return ProductStockModel.builder()
                .id(productStock.getId())
                .name(productStock.getName())
                .sku(productStock.getSku())
                .quantity(productStock.getQuantity())
                .build();
    }
}
