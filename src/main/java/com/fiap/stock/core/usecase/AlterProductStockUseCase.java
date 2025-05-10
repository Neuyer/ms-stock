package com.fiap.stock.core.usecase;

import com.fiap.stock.core.dto.UpdateProductStockDTO;
import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.gateway.StockGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.fiap.stock.core.entity.StockOperation.DECREASE;
import static com.fiap.stock.core.entity.StockOperation.INCREASE;

@Slf4j
@Service
public class AlterProductStockUseCase {
    private final StockGateway stockGateway;

    public AlterProductStockUseCase(StockGateway stockGateway) {
        this.stockGateway = stockGateway;
    }


    public ProductStock execute(String sku, UpdateProductStockDTO input) {
        log.info("Altering stock for sku: {}", sku);
        var stockDB = stockGateway.findBySku(sku).orElseThrow(() -> {
            log.error("Stock not found for sku: {}", sku);
            return new IllegalStateException("Stock not found for sku: " + sku);
        });

        if (INCREASE.equals(input.stockOperation()))
            stockDB.increaseStock(input.quantity());

        if (DECREASE.equals(input.stockOperation()))
            decrease(stockDB, input.quantity());

        stockGateway.save(stockDB);
        log.info("Stock altered with sku: {}", sku);
        return stockDB;
    }

    private void decrease(ProductStock productStock, int qt) {
        if (productStock.getQuantity() < qt)
            throw new IllegalArgumentException("Stock for sku:" + productStock.getSku() + " is less than required: " + qt);
        productStock.decreaseStock(qt);
    }
}
