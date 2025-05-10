package com.fiap.stock.core.usecase;

import com.fiap.stock.core.gateway.StockGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeleteProductStockUseCase {

    private final StockGateway stockGateway;

    public DeleteProductStockUseCase(StockGateway stockGateway) {
        this.stockGateway = stockGateway;
    }

    public void execute(String id) {
        log.info("Deleting stock with id: {}", id);

        stockGateway.findById(id).orElseThrow(() -> {
            log.error("Stock not found with id: {}", id);
            return new IllegalStateException("Stock not found with id: " + id);
        });

        log.info("Stock found with id: {}", id);

        stockGateway.deleteById(id);
    }
}
