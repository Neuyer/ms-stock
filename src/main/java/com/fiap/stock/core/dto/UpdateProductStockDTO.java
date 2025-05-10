package com.fiap.stock.core.dto;

import com.fiap.stock.core.entity.StockOperation;

public record UpdateProductStockDTO(
        StockOperation stockOperation,
        int quantity
) {
}
