package com.fiap.stock.infrastructure.gateway;

import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.gateway.StockGateway;
import com.fiap.stock.infrastructure.mapper.ProductStockMapper;
import com.fiap.stock.infrastructure.repository.StockRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StockGatewayImpl implements StockGateway {

    private final StockRepository stockRepository;

    public StockGatewayImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public Optional<ProductStock> findBySku(String sku) {
        return stockRepository.findBySku(sku)
                .map(ProductStockMapper::toEntity);
    }

    @Override
    public Optional<ProductStock> findById(String id) {
        return stockRepository.findById(id)
                .map(ProductStockMapper::toEntity);
    }

    @Override
    public Set<ProductStock> findAll() {
        return stockRepository.findAll()
                .stream()
                .map(ProductStockMapper::toEntity)
                .collect(Collectors.toSet());
    }

    @Override
    public void save(ProductStock client) {
        stockRepository.save(ProductStockMapper.toModel(client));
    }

    @Override
    public void deleteById(String id) {
        stockRepository.deleteById(id);
    }

}
