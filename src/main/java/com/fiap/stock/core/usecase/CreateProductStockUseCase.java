package com.fiap.stock.core.usecase;

import com.fiap.stock.core.dto.CreateProductStockDTO;
import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.gateway.StockGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CreateProductStockUseCase {

    private final StockGateway stockGateway;

    public CreateProductStockUseCase(StockGateway stockGateway) {
        this.stockGateway = stockGateway;
    }

    public ProductStock execute(CreateProductStockDTO input) {
        log.info("Creating Stock with: {}", input);

        stockGateway.findBySku(input.sku()).ifPresent(productStock -> {
            log.error("Stock for sku {} already exists", input.sku());
            throw new IllegalStateException("Stock for sku already exists");
        });

        var stock = new ProductStock(UUID.randomUUID().toString(), input.sku(), input.name(), input.quantity());

        stockGateway.save(stock);
        log.info("Stock for sku {} created successfully", input.sku());
        return stock;
    }
}
