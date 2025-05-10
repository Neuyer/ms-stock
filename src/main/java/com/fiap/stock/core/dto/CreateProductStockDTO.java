package com.fiap.stock.core.dto;

public record CreateProductStockDTO(
        String sku,
        String name,
        int quantity
) {
}
