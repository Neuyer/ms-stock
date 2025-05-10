package com.fiap.stock.core.usecase;

import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.gateway.StockGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FindStockUseCase {
    private final com.fiap.stock.core.gateway.StockGateway stockGateway;

    public FindStockUseCase(StockGateway stockGateway) {
        this.stockGateway = stockGateway;
    }

    public ProductStock execute(String sku) {
        log.info("Finding stock for sku: {}", sku);

        var productStock = stockGateway.findBySku(sku).orElseThrow(() -> {
            log.error("Stock not for sku doc: {}", sku);
            return new IllegalStateException("Client not found with doc: " + sku);
        });

        log.info("Stock found with sku: {}", sku);
        return productStock;
    }
}
