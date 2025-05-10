package com.fiap.stock.core.usecase;

import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.gateway.StockGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class FindAllStocksUseCase {

    private final StockGateway stockGateway;

    public FindAllStocksUseCase(com.fiap.stock.core.gateway.StockGateway stockGateway) {
        this.stockGateway = stockGateway;
    }

    public Set<ProductStock> execute() {
        log.info("Finding all stocks");
        var stocks = stockGateway.findAll();
        log.info("Found {} stocks", stocks.size());
        return stocks;
    }
}
